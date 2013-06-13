package pt.jma.orchestration.adapter;

import java.lang.reflect.Method;

import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.IServiceInvocation;
import pt.jma.orchestration.activity.config.ActionType;

public abstract class AbstractServiceInvocationImpl implements IServiceInvocation {

	public AbstractServiceInvocationImpl() {
		super();

	}

	public String getTargetClassName() {

		return this.actionType.getTarget().getClazz();
	}

	private Method method = null;

	public Method getMethod() throws Exception {

		if (method == null)
			method = this.getTargetInstance().getClass().getMethod(this.getActionType().getTarget().getMethod(), new Class[] {});

		return method;

	}

	Object targetInstance = null;

	public Object getTargetInstance() throws Exception {
		if (targetInstance == null) {
			targetInstance = ReflectionUtil.getInstance(actionType.getTarget().getClazz());
		}
		return targetInstance;

	}

	ActionType actionType;

	public ActionType getActionType() {
		return actionType;
	}

	public void setTargetInstance(Object targetInstance) {
		this.targetInstance = targetInstance;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

}
