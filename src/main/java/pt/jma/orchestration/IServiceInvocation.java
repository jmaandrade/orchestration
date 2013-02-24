package pt.jma.orchestration;

import java.lang.reflect.Method;

import pt.jma.orchestration.activity.config.ActionType;

public interface IServiceInvocation {

	String getTargetClassName();

	ActionType getActionType();

	Method getMethod() throws Exception;

	Object getTargetInstance() throws Exception;

	void setTargetInstance(Object targetInstance) throws Exception;

	IResponse invoke(IRequest request) throws Exception;

}
