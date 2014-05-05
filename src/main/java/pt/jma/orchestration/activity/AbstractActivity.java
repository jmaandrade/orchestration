package pt.jma.orchestration.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.common.ReflectionUtil;
import pt.jma.common.atomic.IAtomicMapUtil;
import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.activity.config.ActivityType;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.EventType;
import pt.jma.orchestration.context.config.ConverterType;
import pt.jma.orchestration.converters.IConverter;
import pt.jma.orchestration.exception.EventNotFoundException;
import pt.jma.orchestration.service.IService;
import pt.jma.orchestration.util.AbstractConfigurableElement;
import pt.jma.orchestration.util.IConfigurableElement;

public abstract class AbstractActivity {

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

	protected static Map<String, IConverter> converters = new HashMap<String, IConverter>();

	protected static IConverter getConverter(IActivitySettings settings, BindType bindType) throws Exception {

		synchronized (converters) {

			if (!converters.containsKey(bindType.getConverter())) {

				ConverterType converterType = settings.getActivityContext().getConverters().get(bindType.getConverter());

				Object converter = ReflectionUtil.getInstance(converterType.getClazz());

				IConfigurableElement<ConverterType> configurableElement = (IConfigurableElement<ConverterType>) converter;
				configurableElement.setConfig(converterType);

				converters.put(bindType.getConverter(), (IConverter) converter);
			}
		}

		return converters.get(bindType.getConverter());
	}

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
	}

	public UUID getUUID() {
		if (uUID == null)
			uUID = UUID.randomUUID();

		return uUID;
	}

	Map<String, IService> serviceCache = new HashMap<String, IService>();

	protected IService getService(ActionType actionType) throws Throwable {

		String name = actionType.getService();

		synchronized (serviceCache) {
			if (!serviceCache.containsKey(name)) {
				IService service = this.getServiceInstance();
				service.setConfig(this.getSettings().getActivityContext().getServices().get(actionType.getService()));
				service.setActionType(actionType);
				service.setActivity((IActivity) this);
				serviceCache.put(name, (IService) service);

			}
			return serviceCache.get(name);
		}

	}

	abstract IService getServiceInstance() throws Throwable;

}
