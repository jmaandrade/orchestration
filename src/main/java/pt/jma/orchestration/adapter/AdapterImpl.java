package pt.jma.orchestration.adapter;

import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.activity.action.IAction;

public class AdapterImpl extends AbstractAdapter implements IAdapter {

	@Override
	public IAction getNewActionInstance() throws Throwable {

		return (IAction) ReflectionUtil.getInstance(this.getService().getActionType().getTarget().getClazz());

	}
}
