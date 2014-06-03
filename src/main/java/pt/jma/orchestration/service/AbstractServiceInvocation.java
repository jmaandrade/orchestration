package pt.jma.orchestration.service;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.exception.OrchestrationException;

public abstract class AbstractServiceInvocation {

	public AbstractServiceInvocation() {
		// TODO Auto-generated constructor stub
	}

	IService service;

	public IService getService() {
		return service;
	}

	public void setService(IService service) {
		this.service = service;
	}

	IActivity activity;

	public IActivity getActivity() {
		return activity;
	}

	public void setActivity(IActivity activity) {
		this.activity = activity;
	}

	protected ActionType actionType;

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public IResponse invoke(IRequest request) throws OrchestrationException {

		try {
			IResponse response = this.service.getAdapter().invoke((IServiceInvocation) this, request);
			return response;

		} catch (Throwable ex) {
			this.service.getAdapter().handleException(ex);

		}

		return null;

	}

}
