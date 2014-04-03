package pt.jma.orchestration.context;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.context.config.InterceptorType;

public class AddActivityInterceptorProcessor extends AbstractContextProcessor implements IMapProcessor<InterceptorType> {

	public AddActivityInterceptorProcessor(ActivityContext activityContext) {
		super(activityContext);

	}

	public boolean execute(InterceptorType instance) throws Throwable {

		activityContext.getActivityInterceptors().add(instance);

		return true;

	}

}
