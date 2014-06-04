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
import pt.jma.orchestration.result.IResultFn;
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

	Map<String, IResultFn> targetResultFnMap = new HashMap<String, IResultFn>();

	public Map<String, IResultFn> getTargetResultFnMap() {
		return targetResultFnMap;
	}

	public void addEventObserver(IEventActivityObserver userObserver) {
		this.addObserver(new EventActivityObserver(userObserver, this));
	}

	private ActionResult invoke(ActionType actionType, IRequest request, IResponse response) throws OrchestrationException {

		ActionResult result = new ActionResult();
		try {
			scope.put("input-action-message", new PessimisticMapUtilLocking(request));
			scope.put("input-action-message-context", new PessimisticMapUtilLocking(request.getContext()));

			CollectionUtil.map(actionType.getBinds().getInputBind(), this.getInputBindProcessor());
			this.triggerAutomaticEvent("on-action-invoke");

			IService service = this.settings.getActivityContext().getService(actionType.getService());
			IServiceInvocation invocation = service.getNewInvocationInstance(actionType);
			invocation.setActivity((IActivity) this);
			IResponse responseService = invocation.invoke(request);

			response.setOutcome(responseService.getOutcome());

			scope.put("output-action-message", new PessimisticMapUtilLocking(responseService));
			scope.put("output-action-message-context", new PessimisticMapUtilLocking(responseService.getContext()));

			CollectionUtil.map(actionType.getBinds().getOutputBind(), this.getOutputBindProcessor());

		} catch (OrchestrationException ex) {
			throw ex;
		} catch (Throwable ex) {
			result.setError(ex);
		}

		return result;

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

		ActionResult actionResult = null;

		while (true) {

			if (this.settings.getActionMap().containsKey(nextAction) == false)
				throw new ActionNotFoundException(nextAction);

			ActionType actionType = this.settings.getActionMap().get(nextAction);

			response.getContext().put("last-action", actionType.getName());

			if (actionType.getEvent() != null)
				this.triggerUserEvent(actionType.getEvent());

			IRequest serviceRequest = new Request();

			actionResult = this.invoke(actionType, serviceRequest, response);

			nextAction = "";
			ForwardType forwardType = null;

			if (actionType.getForwards().size() == 0) {

			} else if (actionType.getForwards().size() == 1 && actionType.getForwardMap().containsKey(null)) {
				forwardType = actionType.getForwardMap().get(null);
			} else if (actionType.getForwardMap().containsKey(actionResult.getOutcome())) {
				forwardType = actionType.getForwardMap().get(actionResult.getOutcome());
			}

			if (forwardType != null) {
				nextAction = forwardType.getAction();
				this.triggerAutomaticEvent("on-forward");
				this.triggerUserEvent(forwardType.getEvent());
			}

			if (nextAction.isEmpty())
				break;
		}

		ResultType resultType = null;

		if (actionResult.getError() != null) {

			if (this.getSettings().getResultsMap().containsKey("exception")) {

				resultType = CollectionUtil.first(this.getSettings().getResultsMap().get("exception").keySet(),
						new ExceptionResultDetectionProcessor((IActivity) this, actionResult.getError()));

				if (resultType == null) {
					throw actionResult.getError().getCause() == null ? actionResult.getError() : actionResult.getError().getCause();
				}
			}

		} else {

			if (this.getSettings().getResultsMap().containsKey("outcome")) {
				if (this.getSettings().getResultsMap().get("outcome").containsKey(actionResult.getOutcome())) {
					resultType = this.getSettings().getResultsMap().get("outcome").get(actionResult.getOutcome());
				}

			}
		}

		if (resultType != null) {
			CollectionUtil.map(resultType.getBinds(), this.getOutputBindProcessor());
			this.triggerAutomaticEvent("on-result");
			this.triggerUserEvent(resultType.getEvent());

			if (this.getTargetResultFnMap().containsKey(resultType.getTargetType()))
				this.getTargetResultFnMap().get(resultType.getTargetType()).execute(resultType, request, response);
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
