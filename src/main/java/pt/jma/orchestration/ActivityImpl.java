package pt.jma.orchestration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.common.ReflectionUtil;
import pt.jma.common.atomic.IAtomicMapUtil;
import pt.jma.common.atomic.PessimisticMapUtilLocking;
import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.common.collection.IReduceProcessor;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.EventType;
import pt.jma.orchestration.activity.config.ForwardType;
import pt.jma.orchestration.activity.config.StateType;
import pt.jma.orchestration.context.IConverter;
import pt.jma.orchestration.context.config.InterceptorType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.ActionNotFoundException;
import pt.jma.orchestration.exception.EventNotFoundException;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.exception.OutcomeNotFoundException;
import pt.jma.orchestration.result.config.ResultType;
import pt.jma.orchestration.util.thread.IThreadActivityCaller;
import pt.jma.orchestration.util.thread.ThreadActivity;

public class ActivityImpl implements IActivity {

	static Map<String, IConverter> converters = new HashMap<String, IConverter>();

	static IConverter getConverter(IActivitySettings settings, BindType bindType) throws Exception {

		synchronized (converters) {

			if (!converters.containsKey(bindType.getConverter())) {
				converters.put(
						bindType.getConverter(),
						(IConverter) ReflectionUtil.getInstance(settings.getActivityContext().getConverters().get(bindType.getConverter())
								.getClazz()));
			}
		}

		return converters.get(bindType.getConverter());
	}

	public Map<String, IAtomicMapUtil> getScope() {
		return scope;
	}

	IActivitySettings settings;

	public IActivitySettings getSettings() throws Exception {
		return settings;
	}

	public void setSettings(IActivitySettings instance) throws Exception {
		this.settings = instance;
	}

	UUID uUID;

	public ActivityImpl(IActivitySettings settings) {
		super();
		this.settings = settings;

	}

	Map<String, IAtomicMapUtil> scope = new HashMap<String, IAtomicMapUtil>();
	IMapUtil state = new MapUtil();

	static Map<String, String> inputScopesFrom = new HashMap<String, String>();
	static Map<String, String> inputScopesTo = new HashMap<String, String>();
	static Map<String, String> outputScopesFrom = new HashMap<String, String>();
	static Map<String, String> outputScopesTo = new HashMap<String, String>();

	static {

		inputScopesFrom.put("", "input-message");
		inputScopesFrom.put("state", "state");
		inputScopesFrom.put("global-state", "global-state");
		inputScopesFrom.put("properties", "properties");
		inputScopesFrom.put("context", "input-message-context");

		inputScopesTo.put("", "input-action-message");
		inputScopesTo.put("state", "state");
		inputScopesTo.put("global-state", "global-state");
		inputScopesTo.put("context", "input-action-message-context");

		outputScopesFrom.put("", "output-action-message");
		outputScopesFrom.put("state", "state");
		outputScopesFrom.put("global-state", "global-state");
		outputScopesFrom.put("properties", "properties");
		outputScopesFrom.put("context", "output-action-message-context");

		outputScopesTo.put("", "output-message");
		outputScopesTo.put("state", "state");
		outputScopesTo.put("global-state", "global-state");
		outputScopesTo.put("context", "output-message-context");

	}

	private IMapProcessor<BindType> inputBindProcessor = null;
	private IMapProcessor<BindType> outputBindProcessor = null;

	synchronized public IMapProcessor<BindType> getInputBindProcessor() {
		if (this.inputBindProcessor == null)
			this.inputBindProcessor = new BindProcessor(this, inputScopesFrom, inputScopesTo);
		return inputBindProcessor;
	}

	synchronized public IMapProcessor<BindType> getOutputBindProcessor() {
		if (this.outputBindProcessor == null)
			this.outputBindProcessor = new BindProcessor(this, outputScopesFrom, outputScopesTo);
		return outputBindProcessor;
	}

	private IReduceProcessor<InterceptorType, IServiceInvocation> interceptorProcessor = null;

	synchronized public IReduceProcessor<InterceptorType, IServiceInvocation> getInterceptorProcessor() {

		if (this.interceptorProcessor == null)
			this.interceptorProcessor = new InterceptorProcessor(this);

		return interceptorProcessor;
	}

	Map<String, IServiceInvocation> serviceInvocationCache = new HashMap<String, IServiceInvocation>();

	public IServiceInvocation getServiceInvocation(ServiceType serviceType, ActionType actionType, String adapterClassName)
			throws Throwable {

		String name = actionType.getName();

		synchronized (serviceInvocationCache) {
			if (!serviceInvocationCache.containsKey(name)) {
				IServiceInvocation serviceInvocationInstance = (IServiceInvocation) ReflectionUtil.getInstance(adapterClassName);
				serviceInvocationInstance.setActionType(actionType);

				serviceInvocationCache.put(name,
						CollectionUtil.reduce(serviceType.getInterceptors(), this.getInterceptorProcessor(), serviceInvocationInstance));

			}
			return serviceInvocationCache.get(name);
		}

	}

	public IResponse invoke(IRequest request) throws Throwable {

		if (!scope.containsKey("state"))
			scope.put("state", new PessimisticMapUtilLocking(state));

		scope.put("global-state", new PessimisticMapUtilLocking(this.getSettings().getActivityContext().getState()));

		scope.put("input-message", new PessimisticMapUtilLocking(request));
		scope.put("input-message-context", new PessimisticMapUtilLocking(request.getContext()));

		request.getContext().put("activity", this.settings.getName());

		IResponse response = new Response();
		this.uUID = request.getUUID();
		response.setUUID(this.getUUID());

		scope.put("output-message", new PessimisticMapUtilLocking(response));
		scope.put("output-message-context", new PessimisticMapUtilLocking(response.getContext()));

		if (!scope.containsKey("properties"))
			scope.put("properties", new PessimisticMapUtilLocking(this.settings.getProperties()));

		CollectionUtil.map(this.getSettings().getInputBinds(), this.getInputBindProcessor());

		if (this.settings.getStartActivityEvent() != null)
			this.triggerEvent(this.settings.getStartActivityEvent());

		String nextAction = request.getStartAction().isEmpty() ? this.settings.getStart() : request.getStartAction();

		response.getContext().put("activity", this.settings.getName());

		while (true) {

			try {

				if (this.settings.getActionMap().containsKey(nextAction) == false)
					throw new ActionNotFoundException(nextAction);

				ActionType actionType = this.settings.getActionMap().get(nextAction);

				response.getContext().put("last-action", actionType.getName());

				if (actionType.getEvent() != null)
					this.triggerEvent(actionType.getEvent());

				ServiceType serviceType = this.settings.getActivityContext().getServices().get(actionType.getService());

				String adapterClassName = this.settings.getActivityContext().getAdapters().get(serviceType.getAdapter()).getClazz();

				IServiceInvocation serviceInvocationInstance = this.getServiceInvocation(serviceType, actionType, adapterClassName);

				// IServiceInvocation serviceInvocationInstance =
				// (IServiceInvocation)
				// ReflectionUtil.getInstance(adapterClassName);
				// serviceInvocationInstance.setActionType(actionType);
				//
				// serviceInvocationInstance =
				// CollectionUtil.reduce(serviceType.getInterceptors(),
				// this.getInterceptorProcessor(),
				// serviceInvocationInstance);

				IRequest serviceRequest = new Request();

				scope.put("input-action-message", new PessimisticMapUtilLocking(serviceRequest));
				scope.put("input-action-message-context", new PessimisticMapUtilLocking(serviceRequest.getContext()));

				CollectionUtil.map(actionType.getBinds().getInputBind(), this.getInputBindProcessor());

				IResponse responseService = serviceInvocationInstance.invoke(serviceRequest);

				response.setOutcome(responseService.getOutcome());

				scope.put("output-action-message", new PessimisticMapUtilLocking(responseService));
				scope.put("output-action-message-context", new PessimisticMapUtilLocking(responseService.getContext()));

				CollectionUtil.map(actionType.getBinds().getOutputBind(), this.getOutputBindProcessor());

				nextAction = "";
				ForwardType forwardType = null;

				if (actionType.getForwards().size() == 0) {

				} else if (actionType.getForwards().size() == 1) {

					if (actionType.getForwardMap().containsKey(responseService.getOutcome()) == false) {
						if (actionType.getForwardMap().containsKey(null) == false) {
							throw new OutcomeNotFoundException(responseService.getOutcome());
						}
						forwardType = actionType.getForwardMap().get(null);
					} else
						forwardType = actionType.getForwardMap().get(responseService.getOutcome());

				} else if (actionType.getForwardMap().containsKey(responseService.getOutcome()) == false) {
					throw new OutcomeNotFoundException(responseService.getOutcome());
				} else {
					forwardType = actionType.getForwardMap().get(responseService.getOutcome());
				}

				if (forwardType != null)
					nextAction = forwardType.getAction();

				if (nextAction.isEmpty()) {
					if (this.settings.getResultsMap().containsKey("outcome")) {
						if (this.settings.getResultsMap().get("outcome").containsKey(responseService.getOutcome())) {

							ResultType result = this.settings.getResultsMap().get("outcome").get(responseService.getOutcome());

							CollectionUtil.map(result.getBinds(), this.getOutputBindProcessor());

							if (result.getTargetType() != null && result.getTargetType().equalsIgnoreCase("action")) {

								nextAction = result.getTargetName();
							}

							this.triggerEvent(result.getEvent());
						}
					}

				}
				if (forwardType != null)
					this.triggerEvent(forwardType.getEvent());

				if (nextAction.isEmpty())
					break;

			} catch (OrchestrationException ex) {
				throw ex;
			} catch (Throwable ex) {
				ResultType result = null;

				if (this.getSettings().getResultsMap().containsKey("exception")) {

					result = CollectionUtil.first(this.getSettings().getResultsMap().get("exception").keySet(),
							new ExceptionResultDetectionProcessor(this, ex));

					if (result != null) {

						CollectionUtil.map(result.getBinds(), this.getOutputBindProcessor());

						nextAction = (result.getTargetName() != null ? result.getTargetName() : "");
						this.triggerEvent(result.getEvent());
					}

				}

				if (result == null)
					throw ex.getCause();

				if (nextAction.isEmpty())
					break;
			}
		}

		CollectionUtil.map(this.getSettings().getOutputBinds(), this.getOutputBindProcessor());

		return response;

	}

	public IMapProcessor<StateType> stateProcessor = null;

	synchronized public IMapProcessor<StateType> getStateProcessor() {

		if (stateProcessor == null)
			this.stateProcessor = new StateProcessor(this);

		return stateProcessor;
	}

	private void triggerEvent(String name) throws Throwable {

		if (name != null) {

			if (!this.settings.getEventsMap().containsKey(name))
				throw new EventNotFoundException(name);

			EventType event = this.settings.getEventsMap().get(name);

			CollectionUtil.map(event.getStates(), this.getStateProcessor());
			CollectionUtil.map(event.getBinds(),
					(scope.containsKey("output-action-message") ? this.getOutputBindProcessor() : this.getInputBindProcessor()));
		}

	}

	public UUID getUUID() {
		if (uUID == null)
			uUID = UUID.randomUUID();

		return uUID;
	}

	public ThreadActivity invokeAsynchr(IRequest request) throws Throwable {

		ThreadActivity instance = new ThreadActivity(this, request, new IThreadActivityCaller() {
			public boolean notifyThreadEnd() {
				return true;
			}
		});
		instance.start();
		return instance;
	}

}
