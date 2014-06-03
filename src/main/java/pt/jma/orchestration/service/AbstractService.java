package pt.jma.orchestration.service;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.util.AbstractConfigurableElement;

public abstract class AbstractService extends AbstractConfigurableElement<ServiceType> implements IService {

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

	IAdapter adapter;

	public IResponse invoke(IRequest request) throws OrchestrationException {

		try {
			this.getAdapter().beforeInvoke(request);
			IResponse response = this.getAdapter().invoke(request);
			this.getAdapter().afterInvoke(response);
			return response;

		} catch (Throwable ex) {
			this.getAdapter().handleException(ex);

		}

		return null;

	}

	abstract public IAdapter getNewAdapterInstance(AdapterConfigType adapterConfigType) throws Throwable;

	public IAdapter getAdapter() throws OrchestrationException {

		try {
			if (this.adapter == null) {
				AdapterConfigType adapterConfigType = this.getActivity().getSettings().getActivityContext().getAdapters()
						.get(this.getConfig().getAdapter());
				this.adapter = this.getNewAdapterInstance(adapterConfigType);
			}

			return this.adapter;

		} catch (Throwable ex) {
			throw new OrchestrationException(ex);

		}
	}
}
