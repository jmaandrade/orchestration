package pt.jma.orchestration.context;

public abstract class AbstractContextProcessor {

	ActivityContext activityContext;

	public AbstractContextProcessor(ActivityContext activityContext) {
		super();
		this.activityContext = activityContext;
	}

}
