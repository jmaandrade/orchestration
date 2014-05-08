package pt.jma.orchestration.activity;

import java.util.Observable;
import java.util.Observer;

public abstract class AbstractActivityObserver<T extends IActivityObserver> implements Observer {

	static public enum ObserverTypeEnum {
		EVENT
	};

	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		ObserverUpdateArg updateArg = (ObserverUpdateArg) arg;
		if (updateArg.getType().equals(this.getType()))
			this.getObserver().update(updateArg.getValue());
	}

	ObserverTypeEnum type;

	public ObserverTypeEnum getType() {
		return type;
	}

	public void setType(ObserverTypeEnum type) {
		this.type = type;
	}

	public AbstractActivity getActivity() {
		return activity;
	}

	public void setActivity(AbstractActivity activity) {
		this.activity = activity;
	}

	public T getObserver() {
		return observer;
	}

	public void setObserver(T observer) {
		this.observer = observer;
	}

	public AbstractActivity activity;
	T observer;

	public AbstractActivityObserver(ObserverTypeEnum type, AbstractActivity activity, T observer) {
		super();
		this.type = type;
		this.activity = activity;
		this.observer = observer;
	}

}
