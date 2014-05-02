package pt.jma.orchestration.service;

import java.util.Map;

import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.util.PropertiesUtil;

public abstract class AbstractService {

	IActivity activity;

	public IActivity getActivity() {
		return activity;
	}

	public void setActivity(IActivity activity) {
		this.activity = activity;
	}

	Map<String, String> properties = null;

	public Map<String, String> getProperties() {

		if (this.properties == null) {
			this.properties = PropertiesUtil.getPropertiesMap(serviceType == null ? null : serviceType.getProperties());
		}

		return properties;
	}

	ServiceType serviceType;

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
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

	public IAdapter getAdapter() throws OrchestrationException {

		try {
			if 
			
			   (this.adapter == null) {
				this.setServiceType(activity.getSettings().getActivityContext().getServices().get(actionType.getService()));
				
				AdapterConfigType adapterConfigType = activity.getSettings().getActivityContext().getAdapters()
						.get(this.getServiceType().getAdapter());

				this.adapter = (IAdapter) ReflectionUtil.getInstance(adapterConfigType.getClazz());
				this.adapter.setService((IService) this);
				this.adapter.setAdapterConfigType(adapterConfigType);
			}

			return this.adapter;

		} catch (Throwable ex) {
			throw new OrchestrationException(ex);

		}
	}
}
