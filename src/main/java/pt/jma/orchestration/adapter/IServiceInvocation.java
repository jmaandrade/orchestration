package pt.jma.orchestration.adapter;

import java.lang.reflect.Method;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.config.ActionType;

public interface IServiceInvocation {

	String getTargetClassName();

	ActionType getActionType();

	void setActionType(ActionType actionType);

	Method getMethod() throws Exception;

	Object getTargetInstance() throws Exception;

	void setTargetInstance(Object targetInstance) throws Exception;

	IResponse invoke(IRequest request) throws Exception;

}
