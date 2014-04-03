package pt.jma.orchestration.adapter.interceptor;

import pt.jma.orchestration.activity.AbstractInvocationInterceptor;
import pt.jma.orchestration.adapter.IServiceInvocation;

public abstract class AbstractServiceInterceptor extends AbstractInvocationInterceptor {

	IServiceInvocation serviceInvocation = null;

	public IServiceInvocation getServiceInvocation() throws Exception {
		return serviceInvocation;
	}

	public void setServiceInvocation(IServiceInvocation serviceInvocation) throws Exception {
		this.serviceInvocation = serviceInvocation;
		super.setTargetInstance(this.getServiceInvocation());
	}

}
