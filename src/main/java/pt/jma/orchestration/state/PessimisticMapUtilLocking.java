package pt.jma.orchestration.state;

import java.io.Serializable;
import java.util.Comparator;

import pt.jma.common.IMapUtil;

public class PessimisticMapUtilLocking extends PessimisticLocking<IMapUtil> implements IAtomicMapUtil {

	public PessimisticMapUtilLocking(IMapUtil value) {
		super(value);
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

	@Override
	public Serializable swapEntry(AbstractMapUtilEntryFn fn) throws Throwable {

		try {
			synchronized (this.lock) {
				this.value.put(fn.getKey(), fn.call());
				return this.value.get(fn.getKey());
			}
		} catch (Throwable ex) {
			return null;
		}
	}

}
