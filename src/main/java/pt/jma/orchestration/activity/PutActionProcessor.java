package pt.jma.orchestration.activity;

import java.io.Serializable;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.ActionType;

public class PutActionProcessor implements IMapProcessor<ActionType> , Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8531024604248264203L;
	
	IActivitySettings activitySettings;

	public PutActionProcessor(IActivitySettings activitySettings) {
		super();
		this.activitySettings = activitySettings;
	}

	public boolean execute(ActionType instance) throws Throwable {

		activitySettings.getActionMap().put(instance.getName(), instance);

		return true;

	}

}
