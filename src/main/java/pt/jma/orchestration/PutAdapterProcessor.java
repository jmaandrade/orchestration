package pt.jma.orchestration;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.context.config.AdapterConfigType;

public class PutAdapterProcessor extends AbstractContextProcessor implements IMapProcessor<AdapterConfigType> {

	public PutAdapterProcessor(ActivityContext activityContext) {
		super(activityContext);
	}

	public boolean execute(AdapterConfigType instance) throws Throwable {

		activityContext.getAdapters().put(instance.getName(), instance);

		return true;

	}

}
