package pt.jma.orchestration.state;

public class OptimisticLocking<T> extends AbstractAtomic<T> {

	private long version = new Long(0);

	public OptimisticLocking(T value) {
		super(value);

	}

	public T swap(IFn<T> fn) {
		try {
			T swapValue;
			do {
				long previousVersion = this.version;
				swapValue = fn.call();

				synchronized (this.lock) {
					if (previousVersion == this.version) {
						this.version++;
						return this.value = swapValue;
					}
				}

			} while (true);
		} catch (Throwable ex) {
			return null;
		}
	}

}
