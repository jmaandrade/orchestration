package pt.jma.orchestration.activity;

import java.io.Serializable;

import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.StateType;

public class StateProcessor implements IMapProcessor<StateType> , Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4063992472824435543L;
	
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
