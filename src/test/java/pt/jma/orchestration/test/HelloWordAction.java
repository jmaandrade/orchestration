package pt.jma.orchestration.test;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.Response;
import pt.jma.orchestration.activity.action.AbstractAction;
import pt.jma.orchestration.activity.action.IAction;

public class HelloWordAction extends AbstractAction implements IAction {

	public IResponse invoke(IRequest request) throws Exception {

		IResponse response = new Response();
		response.put("sayHi", String.format("Hi %s", request.get("name")));
		response.setOutcome("ok");
		return response;

	}
}
