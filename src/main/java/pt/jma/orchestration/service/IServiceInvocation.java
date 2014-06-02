package pt.jma.orchestration.service;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.exception.OrchestrationException;

public interface IServiceInvocation {
	
	IActivity getActivity();

	void setActivity(IActivity activity);

	ActionType getActionType();

	void setActionType(ActionType actionType);

	IResponse invoke(IRequest request) throws OrchestrationException;
	
	void setService(IService service);

}
