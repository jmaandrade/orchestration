package pt.jma.orchestration;

import pt.jma.common.collection.IMapProcessor;

public class PutActivityPropertyProcessor extends AbstractActivityProcessor implements IMapProcessor<PropertyType> {

	public PutActivityPropertyProcessor(IActivitySettings activitySettings) {
		super(activitySettings);

	}

	public boolean execute(PropertyType propertyType) throws Throwable {
		activitySettings.getProperties().put(propertyType.getName(), propertyType.getValue());
		return true;

	}

}
