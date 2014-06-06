package pt.jma.orchestration.context;

import java.io.Serializable;

public abstract class AbstractContextProcessor  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6122451490427860258L;
	
	AbstractActivityContext activityContext;

	public AbstractContextProcessor(AbstractActivityContext activityContext) {
		super();
		this.activityContext = activityContext;
	}

}
