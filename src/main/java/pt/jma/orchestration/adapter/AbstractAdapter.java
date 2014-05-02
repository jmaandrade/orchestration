package pt.jma.orchestration.adapter;

import java.util.Map;

import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.action.IAction;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.service.IService;
import pt.jma.orchestration.util.PropertiesUtil;

public abstract class AbstractAdapter {

	public void setAdapterConfigType(AdapterConfigType adapterConfigType) {
		this.adapterConfigType = adapterConfigType;
	}

	IService service;

	public IService getService() {
		return service;
	}

	public void setService(IService service) {
		this.service = service;
	}

	public AdapterConfigType getAdapterConfigType() {
		return adapterConfigType;
	}

	Map<String, String> properties = null;

	public Map<String, String> getProperties() {

		if (this.properties == null) {
			this.properties = PropertiesUtil.getPropertiesMap(adapterConfigType == null ? null : adapterConfigType.getProperties());
		}

		return properties;
	}

	AdapterConfigType adapterConfigType;

	public IAction getNewActionInstance() throws Throwable {

		return (IAction) ReflectionUtil.getInstance(this.getService().getActionType().getTarget().getClazz());

	}

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

			action = this.getNewActionInstance();
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
