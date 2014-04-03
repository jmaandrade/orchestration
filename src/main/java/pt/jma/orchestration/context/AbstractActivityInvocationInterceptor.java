package pt.jma.orchestration.context;

import pt.jma.orchestration.activity.AbstractInvocationInterceptor;
import pt.jma.orchestration.activity.IActivity;

public abstract class AbstractActivityInvocationInterceptor extends AbstractInvocationInterceptor {

	public IActivity getActivity() throws Exception {
		return (IActivity) super.getTargetInstance();
	}

	public void setActivity(IActivity instance) throws Exception {
		this.setTargetInstance(instance);
	}

}
