package pt.jma.orchestration.activity;


public abstract class AbstractActivityProcessor {

	IActivitySettings activitySettings;

	public AbstractActivityProcessor(IActivitySettings activitySettings) {
		super();
		this.activitySettings = activitySettings;
	}

}
