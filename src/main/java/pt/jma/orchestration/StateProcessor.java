package pt.jma.orchestration;

import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.StateType;

public class StateProcessor implements IMapProcessor<StateType> {

	IActivity activity;

	public StateProcessor(IActivity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public boolean execute(StateType instance) throws Throwable {

		CollectionUtil.map(instance.getTransitions(), new TransitionProcessor(this.activity, instance));

		return true;

	}

}
