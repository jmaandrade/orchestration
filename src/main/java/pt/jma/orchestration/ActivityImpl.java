package pt.jma.orchestration;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.ForwardType;
import pt.jma.orchestration.activity.config.StateType;
import pt.jma.orchestration.adapter.interceptor.AbstractServiceInterceptor;
import pt.jma.orchestration.context.IConverter;
import pt.jma.orchestration.context.config.InterceptorConfigType;
import pt.jma.orchestration.context.config.InterceptorType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.ActionNotFoundException;
import pt.jma.orchestration.exception.EventNotFoundException;
import pt.jma.orchestration.exception.InvalidScopeException;
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

		inputBind(this.settings.getInputBinds());

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

				inputBind(actionType.getBinds().getInputBind());

				IResponse responseService = serviceInvocationInstance.invoke(serviceRequest);

				response.setOutcome(responseService.getOutcome());

				scope.put("output-action-message", responseService);
				scope.put("output-action-message-context", responseService.getContext());

				ouputBind(actionType.getBinds().getOutputBind());

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
			} catch (Exception ex) {
				nextAction = "";
				// FIXME
				ex.printStackTrace();

				if (this.getSettings().getResultsMap().containsKey("exception")) {

					Iterator<String> iterator = this.getSettings().getResultsMap().get("exception").keySet().iterator();

					while (iterator.hasNext()) {

						String key = iterator.next();
						try {
							Throwable t = ex;

							while (t != null) {
								if (Class.forName(key).isInstance(t)) {

									ResultType result = this.getSettings().getResultsMap().get("exception").get(key);

									if (result.getTargetType().equalsIgnoreCase("action")) {

										nextAction = result.getTargetName();
									}
									this.triggerEvent(result.getEvent());
								}

								t = t.getCause();
							}

						} catch (ClassNotFoundException classNotFound) {
						}

					}

				}

				if (nextAction.isEmpty())
					throw ex.getCause();

			}
		}

		ouputBind(this.getSettings().getOutputBinds());
		return response;

	}

	private void inputBind(List<BindType> binds) throws Exception {

		if (binds != null) {
			Iterator<BindType> iterator = binds.iterator();

			while (iterator.hasNext()) {
				BindType bindType = iterator.next();

				String scopeFrom = "input-message";

				if (bindType.getScopeFrom() != null)
					scopeFrom = bindType.getScopeFrom();

				if (!(scopeFrom.equals("state") || scopeFrom.equals("input-message") || scopeFrom.equals("properties")
						|| scopeFrom.equals("context") || scopeFrom.equals("global-state")))
					throw new InvalidScopeException(scopeFrom);

				String scopeTo = "input-action-message";

				if (bindType.getScopeTo() != null)
					scopeTo = bindType.getScopeTo();

				if (!(scopeTo.equals("state") || scopeTo.equals("input-action-message") || scopeTo.equals("context") || scopeTo
						.equals("global-state")))
					throw new InvalidScopeException(scopeTo);

				this.bindScope(bindType, (scopeFrom.equals("context") ? "input-message-context" : scopeFrom),
						(scopeTo.equals("context") ? "input-action-message-context" : scopeTo));
			}
		}

	}

	private void ouputBind(List<BindType> binds) throws Exception {

		if (binds != null) {
			Iterator<BindType> iterator = binds.iterator();

			while (iterator.hasNext()) {
				BindType bindType = iterator.next();

				String scopeFrom = "output-action-message";

				if (bindType.getScopeFrom() != null)
					scopeFrom = bindType.getScopeFrom();

				if (!(scopeFrom.equals("state") || scopeFrom.equals("output-action-message") || scopeFrom.equals("properties")
						|| scopeFrom.equals("context") || scopeFrom.equals("global-state")))
					throw new InvalidScopeException(scopeFrom);

				String scopeTo = "output-message";

				if (bindType.getScopeTo() != null)
					scopeTo = bindType.getScopeTo();

				if (!(scopeTo.equals("state") || scopeTo.equals("global-state") || scopeTo.equals("output-message") || scopeTo
						.equals("context")))
					throw new InvalidScopeException(scopeTo);

				this.bindScope(bindType, (scopeFrom.equals("context") ? "output-action-message-context" : scopeFrom),
						(scopeTo.equals("context") ? "output-message-context" : scopeTo));
			}
		}

	}

	private void bindScope(BindType bindType, String scopeFrom, String scopeTo) throws Exception {

		synchronized (scope.get(scopeFrom)) {
			synchronized (scope.get(scopeTo)) {

				if (scope.get(scopeFrom).containsKey(bindType.getFrom())) {

					Serializable value = (Serializable) scope.get(scopeFrom).get(bindType.getFrom());

					if (bindType.getConverter() != null) {
						value = (Serializable) ActivityImpl.getConverter(this.getSettings(), bindType).convert(Object.class, value);
					}

					scope.get(scopeTo).put(bindType.getTo(), value);
				}

			}
		}
	}

	private void triggerEvent(String name) throws Exception {

		if (name != null) {

			if (!this.settings.getEventsMap().containsKey(name))
				throw new EventNotFoundException(name);

			this.stateTransition(this.settings.getEventsMap().get(name).getStates());
		}

	}

	private void stateTransition(List<StateType> list) throws Exception {

		if (list.size() > 0)
			for (StateType stateType : list.toArray(new StateType[list.size()])) {

				if (state.containsKey(stateType.getName()) == false)
					state.put(stateType.getName(), "");

				if (stateType.getFrom() == null || state.get(stateType.getName()).equals(stateType.getFrom()))
					state.put(stateType.getName(), stateType.getTo());
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
