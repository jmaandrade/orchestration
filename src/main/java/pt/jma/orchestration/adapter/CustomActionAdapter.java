package pt.jma.orchestration.adapter;

import pt.jma.orchestration.IRequest;
import pt.jma.orchestration.IResponse;
import pt.jma.orchestration.IServiceInvocation;
import pt.jma.orchestration.Response;

public class CustomActionAdapter extends AbstractServiceInvocationImpl implements IServiceInvocation {

	public IResponse invoke(IRequest request) throws Exception {

		Object returnValue = this.getMethod().invoke(this.getTargetInstance());

		IResponse response = new Response();
		response.setOutcome(returnValue == null || !(returnValue instanceof String) ? "success" : ((String) returnValue));
		return response;

	}

}
