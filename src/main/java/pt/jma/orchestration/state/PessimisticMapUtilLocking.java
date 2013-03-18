package pt.jma.orchestration.state;

import java.io.Serializable;

import pt.jma.common.IMapUtil;

public class PessimisticMapUtilLocking extends AbstractMapUtilLocking {

	public PessimisticMapUtilLocking(IMapUtil value) {
		super(value);
	}

	@Override
	public <T extends Serializable> T swapEntry(AbstractMapUtilEntryFn<T> fn) throws Throwable {

		try {
			synchronized (this.lock) {

				this.value.put(fn.getKey(), fn.call());
				return ((T) this.value.get(fn.getKey()));
			}
		} catch (Throwable ex) {
			return null;
		}
	}

}
