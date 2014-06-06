package pt.jma.orchestration.activity;

import java.io.Serializable;

public class EventActivityObserver extends AbstractActivityObserver<IEventActivityObserver> implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4712698423325893354L;

	public EventActivityObserver(IEventActivityObserver observer, AbstractActivity activity) {
		super(ObserverTypeEnum.EVENT, activity, observer);

	}

}
