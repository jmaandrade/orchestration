package pt.jma.orchestration.adapter;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.service.IService;
import pt.jma.orchestration.util.IConfigurableElement;

public interface IAdapter extends IConfigurableElement<AdapterConfigType> {

	IService getService();

	void setService(IService service);

	IResponse invoke(IRequest request) throws OrchestrationException;

	void beforeInvoke(IRequest request);

	void afterInvoke(IResponse response);

	void handleException(Throwable ex) throws OrchestrationException;

}
