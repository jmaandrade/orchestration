package pt.jma.orchestration.context;

import java.io.Serializable;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.context.config.ConverterType;

public class PutConverterProcessor extends AbstractContextProcessor implements
		IMapProcessor<ConverterType>  , Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3565323203035232149L;

	public PutConverterProcessor(AbstractActivityContext activityContext) {
		super(activityContext);

	}

	public boolean execute(ConverterType instance) throws Throwable {

		activityContext.getConverters().put(instance.getName(), instance);

		return true;

	}

}
