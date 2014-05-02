package pt.jma.orchestration.activity.action;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.exception.OrchestrationException;

public interface IAction {

	IAdapter getAdapter();

	void setAdapter(IAdapter adapter);

	IResponse invoke(IRequest request) throws Exception;

	void beforeInvoke(IRequest request);

	void afterInvoke(IResponse response);

	void handleException(Throwable ex) throws OrchestrationException;

}
