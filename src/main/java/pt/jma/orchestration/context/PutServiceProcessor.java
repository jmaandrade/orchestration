package pt.jma.orchestration.context;

import java.io.Serializable;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.context.config.ServiceType;

public class PutServiceProcessor extends AbstractContextProcessor implements IMapProcessor<ServiceType>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1139379088592040685L;

	public PutServiceProcessor(AbstractActivityContext activityContext) {
		super(activityContext);
	}

	public boolean execute(ServiceType instance) throws Throwable {

		activityContext.getServices().put(instance.getName(), instance);

		return true;

	}

}
