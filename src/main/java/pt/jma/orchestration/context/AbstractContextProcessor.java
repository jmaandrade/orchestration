package pt.jma.orchestration.context;

public abstract class AbstractContextProcessor {

	AbstractActivityContext activityContext;

	public AbstractContextProcessor(AbstractActivityContext activityContext) {
		super();
		this.activityContext = activityContext;
	}

}
