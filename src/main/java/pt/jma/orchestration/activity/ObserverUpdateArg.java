package pt.jma.orchestration.activity;

import java.io.Serializable;

import pt.jma.orchestration.activity.AbstractActivityObserver.ObserverTypeEnum;

public class ObserverUpdateArg<T extends Serializable>  implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4664744576013328705L;
	
	ObserverTypeEnum type;
	T value;

	public ObserverTypeEnum getType() {
		return type;
	}

	public void setType(ObserverTypeEnum type) {
		this.type = type;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public ObserverUpdateArg(ObserverTypeEnum type, T value) {
		super();
		this.type = type;
		this.value = value;
	}

}
