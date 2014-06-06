package pt.jma.orchestration.adapter;

import java.io.Serializable;

import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.activity.action.IAction;
import pt.jma.orchestration.activity.config.ActionType;

public class AdapterImpl extends AbstractAdapter implements IAdapter , Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5920356320375073058L;

	@Override
	public IAction getNewActionInstance(ActionType actionType) throws Throwable {

		return (IAction) ReflectionUtil.getInstance(actionType.getTarget().getClazz());

	}
}
