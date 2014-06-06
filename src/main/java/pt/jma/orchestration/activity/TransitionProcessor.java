package pt.jma.orchestration.activity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.StateType;
import pt.jma.orchestration.activity.config.TransitionType;

public class TransitionProcessor implements IMapProcessor<TransitionType> , Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5234451852263741026L;
	
	StateType stateType;
	AbstractActivity activity;

	static Map<String, String> stateTransitionScopesFrom = new HashMap<String, String>();
	static Map<String, String> stateTransitionScopesTo = new HashMap<String, String>();

	static {
		stateTransitionScopesFrom.put("state", "state");
		stateTransitionScopesFrom.put("global-state", "global-state");
		stateTransitionScopesFrom.put("properties", "properties");

		stateTransitionScopesTo.put("state", "state");
		stateTransitionScopesTo.put("global-state", "global-state");
	}

	public TransitionProcessor(AbstractActivity activity, StateType stateType) {
		super();
		this.stateType = stateType;
		this.activity = activity;

	}

	static Comparator<String> stateComparator = new Comparator<String>() {
		public int compare(String arg0, String arg1) {
			return (arg0 == null || arg0.equals(arg1) ? 0 : -1);
		}
	};

	public boolean execute(TransitionType instance) throws Throwable {

		if (this.activity.getScope().get("state")
				.compareAndSet(this.stateType.getName(), stateComparator, instance.getCurrent(), instance.getNext())) {
			CollectionUtil.map(instance.getBinds(), new BindProcessor(this.activity, stateTransitionScopesFrom, stateTransitionScopesTo));
			return false;
		}

		return true;
	}

}
