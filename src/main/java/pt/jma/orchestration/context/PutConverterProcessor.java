package pt.jma.orchestration.context;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.context.config.ConverterType;

public class PutConverterProcessor extends AbstractContextProcessor implements
		IMapProcessor<ConverterType> {

	public PutConverterProcessor(AbstractActivityContext activityContext) {
		super(activityContext);

	}

	public boolean execute(ConverterType instance) throws Throwable {

		activityContext.getConverters().put(instance.getName(), instance);

		return true;

	}

}
