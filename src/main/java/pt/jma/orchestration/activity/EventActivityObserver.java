package pt.jma.orchestration.activity;

public class EventActivityObserver extends AbstractActivityObserver<IEventActivityObserver> {

	public EventActivityObserver(IEventActivityObserver observer, AbstractActivity activity) {
		super(ObserverTypeEnum.EVENT, activity, observer);

	}

}
