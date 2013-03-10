package pt.jma.orchestration;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.context.config.InterceptorType;

public class AddActivityInterceptorProcessor extends AbstractContextProcessor implements IMapProcessor<InterceptorType> {

	public AddActivityInterceptorProcessor(ActivityContext activityContext) {
		super(activityContext);

	}

	@Override
	public boolean execute(InterceptorType instance) throws Throwable {

		activityContext.getActivityInterceptors().add(instance);

		return true;

	}

}
