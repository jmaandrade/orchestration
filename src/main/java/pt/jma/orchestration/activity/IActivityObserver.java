package pt.jma.orchestration.activity;

import java.io.Serializable;

public interface IActivityObserver<T extends Serializable> {

	void update(T value);

}
