package pt.jma.orchestration.service;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.util.IConfigurableElement;

public interface IService extends IConfigurableElement<ServiceType> {

	IActivity getActivity();

	void setActivity(IActivity activity);

	ActionType getActionType();

	void setActionType(ActionType actionType);

	IResponse invoke(IRequest request) throws OrchestrationException;

	IAdapter getAdapter() throws OrchestrationException;

}
