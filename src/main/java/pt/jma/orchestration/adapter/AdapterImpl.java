package pt.jma.orchestration.adapter;

import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.activity.action.IAction;
import pt.jma.orchestration.activity.config.ActionType;

public class AdapterImpl extends AbstractAdapter implements IAdapter {

	@Override
	public IAction getNewActionInstance(ActionType actionType) throws Throwable {

		return (IAction) ReflectionUtil.getInstance(actionType.getTarget().getClazz());

	}
}
