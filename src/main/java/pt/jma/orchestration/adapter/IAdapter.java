package pt.jma.orchestration.adapter;

import java.util.Map;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.action.IAction;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.service.IService;

public interface IAdapter {

	IService getService();

	void setService(IService service);

	AdapterConfigType getAdapterConfigType();

	void setAdapterConfigType(AdapterConfigType adapterConfigType);

	Map<String, String> getProperties() throws Exception;

	IResponse invoke(IRequest request) throws OrchestrationException;

	void beforeInvoke(IRequest request);

	void afterInvoke(IResponse response);

	void handleException(Throwable ex) throws OrchestrationException;

}
