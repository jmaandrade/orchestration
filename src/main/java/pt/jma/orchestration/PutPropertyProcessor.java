package pt.jma.orchestration;

import pt.jma.common.collection.IMapProcessor;

public class PutPropertyProcessor extends AbstractContextProcessor implements IMapProcessor<PropertyType> {

	public PutPropertyProcessor(ActivityContext activityContext) {
		super(activityContext);
	}

	public boolean execute(PropertyType instance) throws Throwable {

		activityContext.getProperties().put(instance.getName(), instance.getValue());

		return true;

	}

}
