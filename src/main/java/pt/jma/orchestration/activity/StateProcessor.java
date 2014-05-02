package pt.jma.orchestration.activity;

import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.StateType;

public class StateProcessor implements IMapProcessor<StateType> {

	AbstractActivity activity;

	public StateProcessor(AbstractActivity activity) {
		super();
		this.activity = activity;
	}

	public boolean execute(StateType instance) throws Throwable {

		CollectionUtil.map(instance.getTransitions(), new TransitionProcessor(this.activity, instance));

		return true;

	}

}
