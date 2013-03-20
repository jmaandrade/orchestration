package pt.jma.orchestration;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.ActionType;

public class PutActionProcessor implements IMapProcessor<ActionType> {

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
