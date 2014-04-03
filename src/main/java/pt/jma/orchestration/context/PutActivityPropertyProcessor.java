package pt.jma.orchestration.context;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.AbstractActivityProcessor;
import pt.jma.orchestration.activity.IActivitySettings;

public class PutActivityPropertyProcessor extends AbstractActivityProcessor implements IMapProcessor<PropertyType> {

	public PutActivityPropertyProcessor(IActivitySettings activitySettings) {
		super(activitySettings);

	}

	public boolean execute(PropertyType propertyType) throws Throwable {
		activitySettings.getProperties().put(propertyType.getName(), propertyType.getValue());
		return true;

	}

}
