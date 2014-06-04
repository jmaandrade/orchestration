package pt.jma.orchestration.result;

import pt.jma.orchestration.activity.IActivity;

public abstract class AbstractResultFn {
	
	IActivity activity;

	public AbstractResultFn(IActivity activity) {
		super();
		this.activity = activity;
	}

	public IActivity getActivity() {
		return activity;
	}

	public void setActivity(IActivity activity) {
		this.activity = activity;
	}

 

}
