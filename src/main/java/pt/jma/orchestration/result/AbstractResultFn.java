package pt.jma.orchestration.result;

import java.io.Serializable;

import pt.jma.orchestration.activity.IActivity;

public abstract class AbstractResultFn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7501831459198125030L;
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
