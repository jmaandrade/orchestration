package pt.jma.orchestration.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.common.collection.CollectionUtil;
import pt.jma.orchestration.activity.ActivitySettings;
import pt.jma.orchestration.activity.AddBindProcessor;
import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IActivitySettings;
import pt.jma.orchestration.activity.PutActionProcessor;
import pt.jma.orchestration.activity.PutEventProcessor;
import pt.jma.orchestration.activity.PutResultProcessor;
import pt.jma.orchestration.activity.config.ActivityType;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.context.config.ContextType;
import pt.jma.orchestration.context.config.ConverterType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.converters.IConverter;
import pt.jma.orchestration.exception.InvalidStartException;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.result.config.ResultType;
import pt.jma.orchestration.service.IService;
import pt.jma.orchestration.util.AbstractConfigurableInheritedElement;

public abstract class AbstractActivityContext extends AbstractConfigurableInheritedElement<ContextType> implements IActivityContext {

	public Map<String, IActivitySettings> mapActivitySettings = new HashMap<String, IActivitySettings>();

	Map<String, ConverterType> converters = new HashMap<String, ConverterType>();
	Map<String, ServiceType> services = new HashMap<String, ServiceType>();
	Map<String, AdapterConfigType> adapters = new HashMap<String, AdapterConfigType>();
	Map<String, Map<String, ResultType>> results = new HashMap<String, Map<String, ResultType>>();

	public Map<String, Map<String, ResultType>> getResults() {
		return results;
	}

	public AbstractActivityContext() throws Exception {

	}

	abstract IService getServiceInstance(ServiceType serviceType) throws Throwable;

	Map<String, IService> serviceCache = new HashMap<String, IService>();

	public IService getService(String name) throws Throwable {

		synchronized (serviceCache) {
			if (!serviceCache.containsKey(name)) {
				IService service = this.getServiceInstance(this.getServices().get(name));
				serviceCache.put(name, (IService) service);

			}
			return serviceCache.get(name);
		}

	}

	abstract public ContextType getContextConfig() throws Exception;

	abstract public ContextType getParentContextConfig(ContextType contextType) throws Exception;

	abstract public ActivityType getActivityConfig(IActivitySettings activitySettings, String name) throws Exception;

	abstract public IActivity getNewActivityInstance(IActivitySettings settings) throws Exception;

	protected void loadContextType(ContextType contextType) throws Exception {

		try {

			if (contextType.getParent() != null)
				this.loadContextType(this.getParentContextConfig(contextType));

			this.getConfigList().add(contextType);

			CollectionUtil.map(contextType.getAdaptersConfig(), new PutAdapterProcessor(this));
			CollectionUtil.map(contextType.getServices(), new PutServiceProcessor(this));
			CollectionUtil.map(contextType.getResults(), new PutResultProcessor(this.getResults()));
			CollectionUtil.map(contextType.getConvertersConfig(), new PutConverterProcessor(this));

		} catch (Throwable ex) {
			throw new OrchestrationException(ex);
		}

	}

	protected void loadActivityType(IActivitySettings activitySettings, String name) throws Throwable {

		ActivityType activityType = this.getActivityConfig(activitySettings, name);

		if (activityType.getParent() != null) {
			loadActivityType(activitySettings, activityType.getParent());
		}

		activitySettings.getConfigList().add(activityType);

		if (activityType.getBinds() != null) {
			CollectionUtil.map(activityType.getBinds().getInputBind(), new AddBindProcessor(activitySettings.getInputBinds()));
			CollectionUtil.map(activityType.getBinds().getOutputBind(), new AddBindProcessor(activitySettings.getOutputBinds()));
		}

		CollectionUtil.map(activityType.getEvents(), new PutEventProcessor(activitySettings));
		CollectionUtil.map(activityType.getProperties(), new PutActivityPropertyProcessor(activitySettings));

		if (activityType.getActions().getStart() != null && activityType.getActions().getStart().isEmpty() == false) {
			activitySettings.setStart(activityType.getActions().getStart());
		}

		if (activityType.getActions().getEvent() != null && activityType.getActions().getEvent().isEmpty() == false) {
			activitySettings.setStartActivityEvent(activityType.getActions().getEvent());
		}
		CollectionUtil.map(activityType.getActions().getActionlist(), new PutActionProcessor(activitySettings));
		CollectionUtil.map(activityType.getResults(), new PutResultProcessor(activitySettings.getResultsMap()));

	}

	public IActivitySettings getActivitySettings(String name) throws Exception {

		try {

			IActivitySettings instance;

			synchronized (mapActivitySettings) {

				if (mapActivitySettings.containsKey(name) == false) {

					instance = new ActivitySettings();

					loadActivityType(instance, name);

					if (instance.getStart() == null || instance.getStart().isEmpty()
							|| instance.getActionMap().containsKey(instance.getStart()) == false)
						throw new InvalidStartException();

					instance.setName(name);

					mapActivitySettings.put(name, instance);

				} else {
					instance = mapActivitySettings.get(name);
				}
				instance.setActivityContext(this);
				return instance;
			}

		} catch (OrchestrationException ex) {
			throw ex;

		} catch (Throwable ex) {
			throw new OrchestrationException(ex);
		}

	}

	public synchronized IActivity lookup(String name) throws Exception {
		try {
			return this.getNewActivityInstance(this.getActivitySettings(name));

		} catch (Throwable ex) {
			throw new OrchestrationException(ex);
		}
	}

	public Map<String, AdapterConfigType> getAdapters() throws Exception {
		return this.adapters;
	}

	public Map<String, ConverterType> getConverters() throws Exception {
		return this.converters;
	}

	public Map<String, ServiceType> getServices() throws Exception {
		return this.services;
	}

	IMapUtil state = new MapUtil();

	public IMapUtil getState() {

		return state;
	}

	static Map<String, IConverter> convertersMap = new HashMap<String, IConverter>();

	abstract IConverter<Serializable> getNewConverterInstance(ConverterType converterType) throws Exception;

	public IConverter<Serializable> getConverter(String name) throws Exception {

		synchronized (convertersMap) {

			if (!convertersMap.containsKey(name)) {

				ConverterType converterType = this.getConverters().get(name);

				convertersMap.put(name, this.getNewConverterInstance(converterType));
			}
		}

		return convertersMap.get(name);
	}
}
