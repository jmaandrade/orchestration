package pt.jma.orchestration;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.common.ReflectionUtil;
import pt.jma.common.collection.CollectionUtil;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.ForwardType;
import pt.jma.orchestration.adapter.interceptor.AbstractServiceInterceptor;
import pt.jma.orchestration.context.IConverter;
import pt.jma.orchestration.context.config.InterceptorConfigType;
import pt.jma.orchestration.context.config.InterceptorType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.ActionNotFoundException;
import pt.jma.orchestration.exception.EventNotFoundException;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.exception.OutcomeNotFoundException;
import pt.jma.orchestration.result.config.ResultType;
import pt.jma.orchestration.util.PropertiesUtil;
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

	public Map<String, IMapUtil> getScope() {
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

	Map<String, IMapUtil> scope = new HashMap<String, IMapUtil>();
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

	@Override
	public synchronized IResponse invoke(IRequest request) throws Throwable {

		if (!scope.containsKey("state"))
			scope.put("state", state);

		scope.put("global-state", this.getSettings().getActivityContext().getState());

		scope.put("input-message", request);
		scope.put("input-message-context", request.getContext());

		request.getContext().put("activity", this.settings.getName());

		IResponse response = new Response();
		this.uUID = request.getUUID();
		response.setUUID(this.getUUID());

		scope.put("output-message", response);
		scope.put("output-message-context", response.getContext());

		if (!scope.containsKey("properties"))
			scope.put("properties", this.settings.getProperties());

		CollectionUtil.map(this.getSettings().getInputBinds(), new BindProcessor(this, inputScopesFrom, inputScopesTo));

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

				InterceptorType[] interceptorsService = serviceType.getInterceptors().toArray(
						new InterceptorType[serviceType.getInterceptors().size()]);

				String adapterClassName = this.settings.getActivityContext().getAdapters().get(serviceType.getAdapter()).getClazz();

				AbstractServiceInvocationImpl abstractServiceInvocationImpl = ReflectionUtil.getInstance(adapterClassName);
				abstractServiceInvocationImpl.setActionType(actionType);

				IServiceInvocation serviceInvocationInstance = (IServiceInvocation) abstractServiceInvocationImpl;

				for (InterceptorType interceptorService : interceptorsService) {

					String interceptorClassName = this.settings.getActivityContext().getInterceptors().get(interceptorService.getName())
							.getClazz();

					AbstractServiceInterceptor handler = ReflectionUtil.getInstance(interceptorClassName);

					InterceptorConfigType interceptorConfigType = this.settings.getActivityContext().getInterceptors()
							.get(interceptorService.getName());

					handler.getPropertiesMap().putAll(PropertiesUtil.getPropertiesMap(interceptorConfigType.getProperties()));
					handler.getPropertiesMap().putAll(PropertiesUtil.getPropertiesMap(interceptorService.getProperties()));

					handler.setServiceInvocation(serviceInvocationInstance);

					serviceInvocationInstance = (IServiceInvocation) Proxy.newProxyInstance(
							((IServiceInvocation) serviceInvocationInstance).getClass().getClassLoader(),
							new Class[] { IServiceInvocation.class }, handler);

				}

				IRequest serviceRequest = new Request();

				scope.put("input-action-message", serviceRequest);
				scope.put("input-action-message-context", serviceRequest.getContext());

				CollectionUtil.map(actionType.getBinds().getInputBind(), new BindProcessor(this, inputScopesFrom, inputScopesTo));

				IResponse responseService = serviceInvocationInstance.invoke(serviceRequest);

				response.setOutcome(responseService.getOutcome());

				scope.put("output-action-message", responseService);
				scope.put("output-action-message-context", responseService.getContext());

				CollectionUtil.map(actionType.getBinds().getOutputBind(), new BindProcessor(this, outputScopesFrom, outputScopesTo));

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

		CollectionUtil.map(this.getSettings().getOutputBinds(), new BindProcessor(this, outputScopesFrom, outputScopesTo));

		return response;

	}

	private void triggerEvent(String name) throws Throwable {

		if (name != null) {

			if (!this.settings.getEventsMap().containsKey(name))
				throw new EventNotFoundException(name);

			CollectionUtil.map(this.settings.getEventsMap().get(name).getStates(), new StateProcessor(this));
		}

	}

	public UUID getUUID() {
		if (uUID == null)
			uUID = UUID.randomUUID();

		return uUID;
	}

	@Override
	public ThreadActivity invokeAsynchr(IRequest request) throws Throwable {

		ThreadActivity instance = new ThreadActivity(this, request, new IThreadActivityCaller() {
			@Override
			public boolean notifyThreadEnd() {
				return true;
			}
		});
		instance.start();
		return instance;
	}

}
