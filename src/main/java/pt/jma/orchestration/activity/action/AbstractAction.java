package pt.jma.orchestration.activity.action;

import java.io.Serializable;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.exception.OrchestrationException;

public abstract class AbstractAction implements IAction, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6163151412790314278L;
	
	IAdapter adapter;

	public IAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(IAdapter adapter) {
		this.adapter = adapter;
	}

	public void beforeInvoke(IRequest request) {
	}

	abstract public IResponse invoke(IRequest request) throws Exception;

	public void afterInvoke(IResponse response) {
	}

	public void handleException(Throwable ex) throws OrchestrationException {

		if (ex instanceof OrchestrationException)
			throw (OrchestrationException) ex;

		throw new OrchestrationException(ex);
	}

}
