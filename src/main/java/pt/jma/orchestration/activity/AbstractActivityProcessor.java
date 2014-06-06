package pt.jma.orchestration.activity;

import java.io.Serializable;

public abstract class AbstractActivityProcessor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2366638737587718600L;
	
	protected IActivitySettings activitySettings;

	public AbstractActivityProcessor(IActivitySettings activitySettings) {
		super();
		this.activitySettings = activitySettings;
	}

}
