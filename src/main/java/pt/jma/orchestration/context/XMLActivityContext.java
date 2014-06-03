package pt.jma.orchestration.context;

import java.io.Serializable;
import java.net.URI;

import pt.jma.common.ReflectionUtil;
import pt.jma.common.xml.SerializationUtils;
import pt.jma.orchestration.activity.ActivityImpl;
import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IActivitySettings;
import pt.jma.orchestration.activity.config.ActivityType;
import pt.jma.orchestration.context.config.ContextType;
import pt.jma.orchestration.context.config.ConverterType;
import pt.jma.orchestration.converters.IConverter;
import pt.jma.orchestration.util.IConfigurableElement;

public class XMLActivityContext extends AbstractActivityContext implements IActivityContext {

	public IActivity getNewActivityInstance(IActivitySettings settings) throws Exception {

		return new ActivityImpl(settings);
	}

	ContextType contextType = null;

	public ContextType getContextConfig() throws Exception {
		if (this.contextType == null)
			this.contextType = SerializationUtils.Deserialize(ContextType.class, this.uri);

		return this.contextType;
	}

	public ContextType getParentContextConfig(ContextType contextType) throws Exception {
		return SerializationUtils.Deserialize(ContextType.class, new URI(contextType.getParent()));
	}

	String activityNameMask = null;
	String activityPath = null;

	public ActivityType getActivityConfig(IActivitySettings activitySettings, String name) throws Exception {

		String nameComplete = String.format(this.activityNameMask, name);

		URI activityURI = new URI(String.format("%s%s", this.activityPath, nameComplete));
		return SerializationUtils.Deserialize(ActivityType.class, activityURI);
	}

	URI uri;

	public URI getURI() {

		return uri;
	}

	public void setURI(URI uri) {

		this.uri = uri;
	}

	public XMLActivityContext(URI uri) throws Exception {

		super();
		this.setURI(uri);
		this.loadContextType(this.getContextConfig());

		this.activityNameMask = (String) this.getProperties().get("activity-name-mask");
		this.activityPath = (String) this.getProperties().get("activity-uri");
	}

	IConverter<Serializable> getNewConverterInstance(ConverterType converterType) throws Exception {

		Object converter = ReflectionUtil.getInstance(converterType.getClazz());

		IConfigurableElement<ConverterType> configurableElement = (IConfigurableElement<ConverterType>) converter;
		configurableElement.setConfig(converterType);
		return (IConverter<Serializable>) configurableElement;
	}

}
