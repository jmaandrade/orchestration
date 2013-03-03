package pt.jma.orchestration;

import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.common.ReflectionUtil;
import pt.jma.common.xml.SerializationUtils;
import pt.jma.common.xml.XMLUtils;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.activity.config.ActivityType;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.EventType;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.context.config.ContextType;
import pt.jma.orchestration.context.config.ConverterType;
import pt.jma.orchestration.context.config.InterceptorConfigType;
import pt.jma.orchestration.context.config.InterceptorType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.InvalidStartException;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.result.config.ResultType;
import pt.jma.orchestration.util.PropertiesUtil;

public class ActivityContext implements IActivityContext {

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

	public ActivityContext(URI uri) throws Exception {

		ActivityContext.loadContextType(this, uri);

	}

	protected static void loadContextType(ActivityContext activityContext, URI uri) throws Exception {

		ContextType contextType = null;

		try {
			Document document = null;

			document = XMLUtils.parseFile(uri.getPath());

			contextType = SerializationUtils.Deserialize(ContextType.class, document);

			if (contextType.getParent() != null)
				ActivityContext.loadContextType(activityContext, new URI(contextType.getParent()));

			for (PropertyType instance : contextType.getProperties().toArray(new PropertyType[contextType.getProperties().size()])) {
				activityContext.getProperties().put(instance.getName(), instance.getValue());
			}

			for (AdapterConfigType instance : contextType.getAdaptersConfig().toArray(
					new AdapterConfigType[contextType.getAdaptersConfig().size()])) {
				activityContext.getAdapters().put(instance.getName(), instance);
			}

			for (InterceptorConfigType instance : contextType.getInterceptorsConfig().toArray(
					new InterceptorConfigType[contextType.getInterceptorsConfig().size()])) {
				activityContext.getInterceptors().put(instance.getName(), instance);
			}

			for (ServiceType instance : contextType.getServices().toArray(new ServiceType[contextType.getServices().size()])) {
				activityContext.getServices().put(instance.getName(), instance);
			}

			for (ResultType instance : contextType.getResults().toArray(new ResultType[contextType.getResults().size()])) {
				if (!activityContext.getResults().containsKey(instance.getType())) {
					activityContext.getResults().put(instance.getType(), new HashMap<String, ResultType>());
				}
				activityContext.getResults().get(instance.getType()).put(instance.getName(), instance);
			}

			for (ConverterType instance : contextType.getConvertersConfig().toArray(
					new ConverterType[contextType.getConvertersConfig().size()])) {
				activityContext.getConverters().put(instance.getName(), instance);
			}

			for (InterceptorType instance : contextType.getActivityInterceptors().toArray(
					new InterceptorType[contextType.getActivityInterceptors().size()])) {
				activityContext.getActivityInterceptors().add(instance);
			}

		} catch (Throwable ex) {
			throw new OrchestrationException(ex);
		}

	}

	public List<InterceptorType> getActivityInterceptors() {
		return activityInterceptors;
	}

	protected void loadActivityType(IActivitySettings activitySettings, String name) throws Exception {

		String activityPathMask = ((String) this.properties.get("activity-name-mask"));
		String path = this.properties.get("activity-uri");
		String nameComplete = String.format(activityPathMask, name);

		URI activityURI = new URI(String.format("%s%s", path, nameComplete));

		ActivityType activityType = SerializationUtils.Deserialize(ActivityType.class, activityURI);

		if (activityType.getParent() != null) {
			loadActivityType(activitySettings, activityType.getParent());
		}

		if (activityType.getBinds() != null) {

			if (activityType.getBinds().getInputBind() != null) {

				for (BindType bindType : activityType.getBinds().getInputBind()
						.toArray(new BindType[activityType.getBinds().getInputBind().size()])) {
					activitySettings.getInputBinds().add(bindType);
				}
			}

			if (activityType.getBinds().getOutputBind() != null) {

				for (BindType bindType : activityType.getBinds().getOutputBind()
						.toArray(new BindType[activityType.getBinds().getOutputBind().size()])) {
					activitySettings.getOutputBinds().add(bindType);
				}
			}
		}

		if (activityType.getEvents().size() > 0) {
			for (EventType eventType : activityType.getEvents().toArray(new EventType[activityType.getEvents().size()])) {
				activitySettings.getEventsMap().put(eventType.getName(), eventType);
			}
		}

		PropertyType[] arrayPropertyType = activityType.getProperties().toArray(new PropertyType[activityType.getProperties().size()]);

		for (PropertyType propertyType : arrayPropertyType) {
			activitySettings.getProperties().put(propertyType.getName(), propertyType.getValue());
		}

		if (activityType.getActions().getStart() != null && activityType.getActions().getStart().isEmpty() == false) {
			activitySettings.setStart(activityType.getActions().getStart());
		}

		if (activityType.getActions().getEvent() != null && activityType.getActions().getEvent().isEmpty() == false) {
			activitySettings.setStartActivityEvent(activityType.getActions().getEvent());
		}

		ActionType[] array = activityType.getActions().getActionlist()
				.toArray(new ActionType[activityType.getActions().getActionlist().size()]);

		for (ActionType actionType : array) {
			activitySettings.getActionMap().put(actionType.getName(), actionType);
		}

		ResultType[] arrayResult = activityType.getResults() == null ? new ResultType[0] : activityType.getResults().toArray(
				new ResultType[activityType.getResults().size()]);

		for (ResultType instance : arrayResult) {
			if (!activitySettings.getResultsMap().containsKey(instance.getType())) {
				activitySettings.getResultsMap().put(instance.getType(), new HashMap<String, ResultType>());
			}
			activitySettings.getResultsMap().get(instance.getType()).put(instance.getName(), instance);
		}

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

	@Override
	public synchronized IActivity lookup(String name) throws Exception {

		IActivity instance = (IActivity) new ActivityImpl(this.getActivitySettings(name));
		for (InterceptorType interceptor : this.activityInterceptors.toArray(new InterceptorType[this.activityInterceptors.size()])) {

			String interceptorClassName = this.interceptors.get(interceptor.getName()).getClazz();

			AbstractActivityInvocationInterceptor handler = ReflectionUtil.getInstance(interceptorClassName);
			handler.setActivity(instance);

			handler.getPropertiesMap().putAll(PropertiesUtil.getPropertiesMap(interceptor.getProperties()));

			instance = (IActivity) Proxy.newProxyInstance(((IActivity) instance).getClass().getClassLoader(),
					new Class[] { IActivity.class }, handler);
		}

		return instance;

	}

	@Override
	public Map<String, AdapterConfigType> getAdapters() throws Exception {
		return this.adapters;
	}

	@Override
	public Map<String, ConverterType> getConverters() throws Exception {
		return this.converters;
	}

	@Override
	public Map<String, InterceptorConfigType> getInterceptors() throws Exception {
		return this.interceptors;
	}

	@Override
	public Map<String, ServiceType> getServices() throws Exception {
		return this.services;
	}

	IMapUtil state = new MapUtil();

	@Override
	public IMapUtil getState() {

		return state;
	}

}
