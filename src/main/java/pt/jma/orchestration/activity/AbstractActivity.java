package pt.jma.orchestration.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.common.atomic.IAtomicMapUtil;
import pt.jma.common.atomic.PessimisticMapUtilLocking;
import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.AbstractActivityObserver.ObserverTypeEnum;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.EventType;
import pt.jma.orchestration.activity.config.ForwardType;
import pt.jma.orchestration.exception.ActionNotFoundException;
import pt.jma.orchestration.exception.EventNotFoundException;
import pt.jma.orchestration.exception.ExceptionResultDetectionProcessor;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.exception.OutcomeNotFoundException;
import pt.jma.orchestration.result.config.ResultType;
import pt.jma.orchestration.service.IService;
import pt.jma.orchestration.service.IServiceInvocation;
import pt.jma.orchestration.util.thread.IThreadActivityCaller;
import pt.jma.orchestration.util.thread.ThreadActivity;

public abstract class AbstractActivity extends Observable {

	protected Map<String, IAtomicMapUtil> scope = new HashMap<String, IAtomicMapUtil>();

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

	IMapUtil state = new MapUtil();

	protected static Map<String, String> inputScopesFrom = new HashMap<String, String>();
	protected static Map<String, String> inputScopesTo = new HashMap<String, String>();
	protected static Map<String, String> outputScopesFrom = new HashMap<String, String>();
	protected static Map<String, String> outputScopesTo = new HashMap<String, String>();

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

	protected IMapProcessor<BindType> inputBindProcessor = null;
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

	ActivityEventProcessor activityEventProcessor = null;

	synchronized protected ActivityEventProcessor getActivityEventProcessor() {

		if (activityEventProcessor == null)
			this.activityEventProcessor = new ActivityEventProcessor(this);

		activityEventProcessor.setBindProcessor((scope.containsKey("output-action-message") ? this.getOutputBindProcessor() : this
				.getInputBindProcessor()));
		return activityEventProcessor;
	}

	protected void triggerAutomaticEvent(String name) throws Throwable {
		if (this.settings.getEventsMap().containsKey(name)) {
			triggerEvent(this.settings.getEventsMap().get(name));
		}
	}

	protected void triggerUserEvent(String name) throws Throwable {
		if (name != null) {
			if (!this.settings.getEventsMap().containsKey(name))
				throw new EventNotFoundException(name);
			triggerEvent(this.settings.getEventsMap().get(name));
		}
	}

	protected void triggerEvent(EventType event) throws Throwable {
		CollectionUtil.map(event.getBindsAndStates(), this.getActivityEventProcessor());
		setChanged();
		notifyObservers(new ObserverUpdateArg<String>(ObserverTypeEnum.EVENT, event.getName()));
	}

	public UUID getUUID() {
		if (uUID == null)
			uUID = UUID.randomUUID();

		return uUID;
	}

	public void addEventObserver(IEventActivityObserver userObserver) {
		this.addObserver(new EventActivityObserver(userObserver, this));
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

		Boolean startCond = !state.containsKey("start");

		state.put("start", startCond.toString());
		this.triggerAutomaticEvent(startCond ? "on-activity-start" : "on-activity-restart");

		if (this.settings.getStartActivityEvent() != null)
			this.triggerUserEvent(this.settings.getStartActivityEvent());

		String nextAction = request.getStartAction().isEmpty() ? this.settings.getStart() : request.getStartAction();

		while (true) {

			try {

				if (this.settings.getActionMap().containsKey(nextAction) == false)
					throw new ActionNotFoundException(nextAction);

				ActionType actionType = this.settings.getActionMap().get(nextAction);

				response.getContext().put("last-action", actionType.getName());

				if (actionType.getEvent() != null)
					this.triggerUserEvent(actionType.getEvent());

				IRequest serviceRequest = new Request();

				scope.put("input-action-message", new PessimisticMapUtilLocking(serviceRequest));
				scope.put("input-action-message-context", new PessimisticMapUtilLocking(serviceRequest.getContext()));

				CollectionUtil.map(actionType.getBinds().getInputBind(), this.getInputBindProcessor());
				this.triggerAutomaticEvent("on-action-invoke");

				IService service = this.settings.getActivityContext().getService(actionType.getService());
				IServiceInvocation invocation = service.getNewInvocationInstance(actionType);
				invocation.setActivity((IActivity) this);
				IResponse responseService = invocation.invoke(serviceRequest);
				
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

				if (forwardType != null) {
					nextAction = forwardType.getAction();
					this.triggerAutomaticEvent("on-forward");
				}
				if (nextAction.isEmpty()) {
					if (this.settings.getResultsMap().containsKey("outcome")) {
						if (this.settings.getResultsMap().get("outcome").containsKey(responseService.getOutcome())) {

							ResultType result = this.settings.getResultsMap().get("outcome").get(responseService.getOutcome());

							CollectionUtil.map(result.getBinds(), this.getOutputBindProcessor());

							if (result.getTargetType() != null && result.getTargetType().equalsIgnoreCase("action")) {

								nextAction = result.getTargetName();
								this.triggerAutomaticEvent("on-result");
							}

							this.triggerUserEvent(result.getEvent());
						}
					}

				}

				if (forwardType != null)
					this.triggerUserEvent(forwardType.getEvent());

				if (nextAction.isEmpty())
					break;

			} catch (OrchestrationException ex) {
				throw ex;
			} catch (Throwable ex) {
				ResultType result = null;

				if (this.getSettings().getResultsMap().containsKey("exception")) {

					result = CollectionUtil.first(this.getSettings().getResultsMap().get("exception").keySet(),
							new ExceptionResultDetectionProcessor((IActivity) this, ex));

					if (result != null) {
						this.triggerAutomaticEvent("on-result");

						CollectionUtil.map(result.getBinds(), this.getOutputBindProcessor());

						nextAction = (result.getTargetName() != null ? result.getTargetName() : "");
						this.triggerUserEvent(result.getEvent());
					}

				}

				if (result == null)
					throw ex.getCause() == null ? ex : ex.getCause();

				if (nextAction.isEmpty())
					break;
			}
		}
		this.triggerAutomaticEvent("on-activity-end");
		CollectionUtil.map(this.getSettings().getOutputBinds(), this.getOutputBindProcessor());
		return response;

	}

	public ThreadActivity invokeAsynchr(IRequest request) throws Throwable {

		return this.invokeAsynchr(request, new IThreadActivityCaller() {
			public boolean notifyThreadEnd() {
				return true;
			}
		});
	}

	public ThreadActivity invokeAsynchr(IRequest request, IThreadActivityCaller caller) throws Throwable {

		ThreadActivity instance = new ThreadActivity((IActivity) this, request, caller);
		instance.start();
		return instance;
	}

}
