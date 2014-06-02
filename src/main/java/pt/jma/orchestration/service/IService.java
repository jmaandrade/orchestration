package pt.jma.orchestration.service;

import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.context.IActivityContext;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.util.IConfigurableElement;

public interface IService extends IConfigurableElement<ServiceType> {
	
	IActivityContext getContext();

	IAdapter getAdapter() throws OrchestrationException;
	
	IServiceInvocation getNewInvocationInstance(ActionType actionType) throws Throwable;

}
