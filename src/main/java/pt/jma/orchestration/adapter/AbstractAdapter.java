package pt.jma.orchestration.adapter;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.action.IAction;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.service.IService;
import pt.jma.orchestration.util.AbstractConfigurableElement;
import pt.jma.orchestration.util.IConfigurableElement;

public abstract class AbstractAdapter extends AbstractConfigurableElement<AdapterConfigType> implements
		IConfigurableElement<AdapterConfigType> {

	IService service;

	public IService getService() {
		return service;
	}

	public void setService(IService service) {
		this.service = service;
	}

	abstract public IAction getNewActionInstance(ActionType actionType) throws Throwable;

	public void beforeInvoke(IRequest request) {
	}

	public void afterInvoke(IResponse response) {
	}

	public void handleException(Throwable ex) throws OrchestrationException {

		if (ex instanceof OrchestrationException)
			throw (OrchestrationException) ex;

		throw new OrchestrationException(ex);
	}

	public IResponse invoke(IRequest request) throws OrchestrationException {

		IAction action = null;

		try {

			action = this.getNewActionInstance(this.getService().getActionType());
			action.setAdapter((IAdapter) this);
			action.beforeInvoke(request);
			IResponse response = action.invoke(request);
			action.afterInvoke(response);
			return response;

		} catch (Throwable ex) {
			if (action != null)
				action.handleException(ex);
			else
				this.handleException(ex);

		}

		return null;
	}
}
