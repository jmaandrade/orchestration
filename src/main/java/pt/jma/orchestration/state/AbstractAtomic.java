package pt.jma.orchestration.state;

import java.util.Comparator;

public abstract class AbstractAtomic<T> implements IAtomic<T> {

	protected Object lock = new Object();
	protected T value;

	public AbstractAtomic(T value) {
		super();
		this.value = value;

	}

	public T getValue() {
		return value;
	}

	@Override
	public boolean compareAndSet(Comparator<T> comparator, T current, T next) {
		synchronized (this.lock) {
			if (comparator.compare(this.value, current) == 0) {
				this.value = next;
				return true;
			}
		}
		return false;
	}

	public abstract T swap(IFn<T> fn);

}
