package pt.jma.orchestration;

import java.util.HashMap;
import java.util.Map;

import pt.jma.common.IMapUtil;
import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.StateType;
import pt.jma.orchestration.activity.config.TransitionType;

public class TransitionProcessor implements IMapProcessor<TransitionType> {

	StateType stateType;
	IActivity activity;

	static Map<String, String> stateTransitionScopesFrom = new HashMap<String, String>();
	static Map<String, String> stateTransitionScopesTo = new HashMap<String, String>();

	static {
		stateTransitionScopesFrom.put("state", "state");
		stateTransitionScopesFrom.put("global-state", "global-state");
		stateTransitionScopesFrom.put("properties", "properties");

		stateTransitionScopesTo.put("state", "state");
		stateTransitionScopesTo.put("global-state", "global-state");
	}

	public TransitionProcessor(IActivity activity, StateType stateType) {
		super();
		this.stateType = stateType;
		this.activity = activity;

	}

	@Override
	public boolean execute(TransitionType instance) throws Throwable {

		IMapUtil state = this.activity.getScope().get("state");

		if (!state.containsKey(this.stateType.getName())) {
			state.put(this.stateType.getName(), "");
		}
		synchronized (state) {

			if ((instance.getCurrent() != null && state.get(this.stateType.getName()).equals(instance.getCurrent()))
					|| (instance.getCurrent() == null)) {
				state.put(this.stateType.getName(), instance.getNext());

				CollectionUtil.map(instance.getBinds(),
						new BindProcessor(this.activity, stateTransitionScopesFrom, stateTransitionScopesTo));

				return false;
			}

		}
		return true;
	}

}
