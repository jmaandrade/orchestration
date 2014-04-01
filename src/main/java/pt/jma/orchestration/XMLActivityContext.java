package pt.jma.orchestration;

import java.net.URI;
import pt.jma.common.xml.SerializationUtils;

public class XMLActivityContext extends AbstractActivityContext implements IActivityContext {


	public ContextType getContextConfig() throws Exception {
		return SerializationUtils.Deserialize(ContextType.class, this.uri.getPath());
	}
	
	public ActivityType getActivityConfig(IActivitySettings activitySettings, String name) throws Exception {
		
		String activityPathMask = ((String) super.properties.get("activity-name-mask"));
		String path = super.properties.get("activity-uri");
		String nameComplete = String.format(activityPathMask, name);

		URI activityURI = new URI(String.format("%s%s", path, nameComplete));
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
	}

}

