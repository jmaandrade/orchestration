package pt.jma.orchestration.state;

import java.io.Serializable;
import java.util.Comparator;

import pt.jma.common.IMapUtil;

public abstract class AbstractMapUtilLocking implements IAtomicMapUtil {

	protected Object lock = new Object();
	protected IMapUtil value;

	public AbstractMapUtilLocking(IMapUtil value) {
		super();
		this.value = value;
	}

	public IMapUtil getValue() {
		return value;
	}

	@Override
	public <T extends Serializable> boolean compareAndSet(String key, Comparator<T> comparator, T current, T next) throws Throwable {

		synchronized (this.lock) {

			if (comparator.compare(((T) this.value.get(key)), current) == 0) {
				this.value.put(key, next);
				return true;
			}
		}
		return false;

	}

}
