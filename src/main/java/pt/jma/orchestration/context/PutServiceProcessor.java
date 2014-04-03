package pt.jma.orchestration.context;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.context.config.ServiceType;

public class PutServiceProcessor extends AbstractContextProcessor implements IMapProcessor<ServiceType> {

	public PutServiceProcessor(ActivityContext activityContext) {
		super(activityContext);
	}

	public boolean execute(ServiceType instance) throws Throwable {

		activityContext.getServices().put(instance.getName(), instance);

		return true;

	}

}
