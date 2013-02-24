package pt.jma.orchestration;

public abstract class AbstractActivityInvocationInterceptor extends AbstractInvocationInterceptor {

	public IActivity getActivity() throws Exception {
		return (IActivity) super.getTargetInstance();
	}

	public void setActivity(IActivity instance) throws Exception {
		this.setTargetInstance(instance);
	}

}
