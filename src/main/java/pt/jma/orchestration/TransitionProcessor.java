package pt.jma.orchestration;

import java.util.HashMap;
import java.util.Map;

import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.StateType;
import pt.jma.orchestration.activity.config.TransitionType;

public class TransitionProcessor implements IMapProcessor<TransitionType> {

	StateType stateType;
	IActivity activity;

	Map<String, String> stateTransitionScopesFrom = new HashMap<String, String>();
	Map<String, String> stateTransitionScopesTo = new HashMap<String, String>();

	public TransitionProcessor(IActivity activity, StateType stateType) {
		super();
		this.stateType = stateType;
		this.activity = activity;

		stateTransitionScopesFrom.put("state", "state");
		stateTransitionScopesFrom.put("global-state", "global-state");
		stateTransitionScopesFrom.put("properties", "properties");

		stateTransitionScopesTo.put("state", "state");
		stateTransitionScopesTo.put("global-state", "global-state");
	}

	@Override
	public boolean execute(TransitionType instance) throws Throwable {

		if ((instance.getCurrent() != null && this.activity.getScope().get("state").get(this.stateType.getName())
				.equals(instance.getCurrent()))
				|| (instance.getCurrent() == null)) {
			this.activity.getScope().get("state").put(this.stateType.getName(), instance.getNext());

			CollectionUtil.map(instance.getBinds(), new BindProcessor(this.activity, stateTransitionScopesFrom, stateTransitionScopesTo));

			return false;
		}

		return true;
	}

}
