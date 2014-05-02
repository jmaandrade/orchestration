package pt.jma.orchestration.context;

import pt.jma.common.collection.IMapProcessor;

public class PutPropertyProcessor extends AbstractContextProcessor implements
		IMapProcessor<PropertyType> {

	public PutPropertyProcessor(AbstractActivityContext activityContext) {
		super(activityContext);
	}

	public boolean execute(PropertyType instance) throws Throwable {

		activityContext.getProperties().put(instance.getName(),
				instance.getValue());

		return true;

	}

}
