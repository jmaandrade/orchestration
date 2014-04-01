package pt.jma.orchestration;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.common.collection.CollectionUtil;
import pt.jma.orchestration.activity.config.ActivityType;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.context.config.ContextType;
import pt.jma.orchestration.context.config.ConverterType;
import pt.jma.orchestration.context.config.InterceptorConfigType;
import pt.jma.orchestration.context.config.InterceptorType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.InvalidStartException;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.result.config.ResultType;

public abstract class AbstractActivityContext {

	public Map<String, IActivitySettings> mapActivitySettings = new HashMap<String, IActivitySettings>();

	Map<String, String> properties = new HashMap<String, String>();
	Map<String, ConverterType> converters = new HashMap<String, ConverterType>();
	Map<String, ServiceType> services = new HashMap<String, ServiceType>();
	Map<String, InterceptorConfigType> interceptors = new HashMap<String, InterceptorConfigType>();
	Map<String, AdapterConfigType> adapters = new HashMap<String, AdapterConfigType>();
	Map<String, Map<String, ResultType>> results = new HashMap<String, Map<String, ResultType>>();

	List<InterceptorType> activityInterceptors = new ArrayList<InterceptorType>();

	public Map<String, Map<String, ResultType>> getResults() {
		return results;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public AbstractActivityContext() throws Exception {
	
	}
	
	abstract public ContextType getContextConfig() throws Exception;
	abstract public ActivityType getActivityConfig(IActivitySettings activitySettings, String name) throws Exception;
	abstract public IActivity getNewActivityInstance(IActivitySettings settings) throws Exception;

	protected static void loadContextType(AbstractActivityContext activityContext) throws Exception {

		ContextType contextType = null;

		try {

			contextType = activityContext.getContextConfig();

			if (contextType.getParent() != null)
				AbstractActivityContext.loadContextType(activityContext, new URI(contextType.getParent()));

			CollectionUtil.map(contextType.getProperties(), new PutPropertyProcessor(activityContext));
			CollectionUtil.map(contextType.getAdaptersConfig(), new PutAdapterProcessor(activityContext));
			CollectionUtil.map(contextType.getInterceptorsConfig(), new PutInterceptorProcessor(activityContext.getInterceptors()));
			CollectionUtil.map(contextType.getServices(), new PutServiceProcessor(activityContext));
			CollectionUtil.map(contextType.getResults(), new PutResultProcessor(activityContext.getResults()));
			CollectionUtil.map(contextType.getConvertersConfig(), new PutConverterProcessor(activityContext));
			CollectionUtil.map(contextType.getActivityInterceptors(), new AddActivityInterceptorProcessor(activityContext));

		} catch (Throwable ex) {
			throw new OrchestrationException(ex);
		}

	}

	public List<InterceptorType> getActivityInterceptors() {
		return activityInterceptors;
	}

	protected void loadActivityType(IActivitySettings activitySettings, String name) throws Throwable {

		ActivityType activityType = this.getActivityConfig(activitySettings, name);

		if (activityType.getParent() != null) {
			loadActivityType(activitySettings, activityType.getParent());
		}

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
			IActivity instance =  this.getNewActivityInstance(this.getActivitySettings(name));

			return CollectionUtil.reduce(this.activityInterceptors, new InjectInterceptoresProcessor(this), instance);

		} catch (Throwable ex) {
			throw new Exception(ex);
		}
	}

	public Map<String, AdapterConfigType> getAdapters() throws Exception {
		return this.adapters;
	}

	public Map<String, ConverterType> getConverters() throws Exception {
		return this.converters;
	}

	public Map<String, InterceptorConfigType> getInterceptors() throws Exception {
		return this.interceptors;
	}

	public Map<String, ServiceType> getServices() throws Exception {
		return this.services;
	}

	IMapUtil state = new MapUtil();

	public IMapUtil getState() {

		return state;
	}

}
