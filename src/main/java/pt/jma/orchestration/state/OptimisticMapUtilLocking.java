package pt.jma.orchestration.state;

import java.io.Serializable;

import pt.jma.common.IMapUtil;

public class OptimisticMapUtilLocking extends AbstractMapUtilLocking {

	private long version = new Long(0);

	public OptimisticMapUtilLocking(IMapUtil value) {
		super(value);
	}

	@Override
	public <T extends Serializable> T swapEntry(IMapUtilEntryFn<T> fn) throws Throwable {

		try {

			T swapValue;
			do {
				long previousVersion = this.version;
				swapValue = fn.call();

				synchronized (this.lock) {
					if (previousVersion == this.version) {
						this.version++;
						this.value.put(fn.getKey(), swapValue);
						return ((T) this.value.get(fn.getKey()));
					}
				}

			} while (true);
		} catch (Throwable ex) {
			return null;
		}
	}

}
