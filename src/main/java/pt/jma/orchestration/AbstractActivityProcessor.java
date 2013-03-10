package pt.jma.orchestration;

public abstract class AbstractActivityProcessor {

	IActivitySettings activitySettings;

	public AbstractActivityProcessor(IActivitySettings activitySettings) {
		super();
		this.activitySettings = activitySettings;
	}

}
