package pt.jma.orchestration.state;

public class PessimisticLocking<T> extends AbstractAtomic<T> {

	public PessimisticLocking(T value) {
		super(value);

	}

	public T swap(IFn<T> fn) {
		try {
			synchronized (this.lock) {
				return this.value = fn.call();
			}
		} catch (Throwable ex) {
			return null;
		}

	}

}
