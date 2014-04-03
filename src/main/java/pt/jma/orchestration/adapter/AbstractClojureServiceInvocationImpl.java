package pt.jma.orchestration.adapter;

import java.lang.reflect.Method;

import pt.jma.orchestration.activity.config.ActionType;
import clojure.lang.RT;
import clojure.lang.Symbol;

public abstract class AbstractClojureServiceInvocationImpl implements IServiceInvocation {

	public AbstractClojureServiceInvocationImpl() {
		super();

	}

	public String getTargetClassName() {

		return this.actionType.getTarget().getClazz();
	}

	public Method getMethod() throws Exception {
		return this.getTargetInstance().getClass().getMethod("invoke", Object.class);
	}

	Object targetInstance = null;

	public Object getTargetInstance() throws Exception {
		if (targetInstance == null) {
			RT.var("clojure.core", "require").invoke(Symbol.intern(this.getTargetClassName()));
			targetInstance = RT.var(this.getTargetClassName(), this.actionType.getTarget().getMethod());
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
