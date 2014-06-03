package pt.jma.orchestration.service;

import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.context.IActivityContext;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.util.AbstractConfigurableElement;

public abstract class AbstractService extends AbstractConfigurableElement<ServiceType> implements IService {

	IActivityContext context;

	public IActivityContext getContext() {
		return context;
	}

	public void setContext(IActivityContext context) {
		this.context = context;
	}

	public void setAdapter(IAdapter adapter) {
		this.adapter = adapter;
	}

	public AbstractService(IActivityContext context) {
		super();
		this.context = context;
	}

	IAdapter adapter;

	abstract public IAdapter getNewAdapterInstance(AdapterConfigType adapterConfigType) throws Throwable;

	abstract public IServiceInvocation getNewInvocationInstance(ActionType actionType) throws Throwable;

	public IAdapter getAdapter() throws OrchestrationException {

		try {
			if (this.adapter == null) {
				AdapterConfigType adapterConfigType = this.context.getAdapters().get(this.getConfig().getAdapter());
				this.adapter = this.getNewAdapterInstance(adapterConfigType);
			}

			return this.adapter;

		} catch (Throwable ex) {
			throw new OrchestrationException(ex);

		}
	}
}
