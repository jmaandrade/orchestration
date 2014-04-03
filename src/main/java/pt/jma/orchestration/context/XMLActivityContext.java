package pt.jma.orchestration.context;

import java.net.URI; 

import pt.jma.common.xml.SerializationUtils;
import pt.jma.orchestration.activity.ActivityImpl;
import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IActivitySettings;

public class XMLActivityContext extends AbstractActivityContext implements IActivityContext {

	public IActivity getNewActivityInstance(IActivitySettings settings) throws Exception {
	
		return new ActivityImpl(settings);
	}

	public ContextType getContextConfig() throws Exception {
		return SerializationUtils.Deserialize(ContextType.class, this.uri.getPath());
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
	
		super(this);
		this.setURI(uri);
		AbstractActivityContext.loadContextType(this);
		
		this.activityNameMask = super.properties.get("activity-name-mask");
		this.activityPath = super.properties.get("activity-uri");
	}

}

