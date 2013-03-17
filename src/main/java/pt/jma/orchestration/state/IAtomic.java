package pt.jma.orchestration.state;

import java.util.Comparator;

public interface IAtomic<T> {

	T swap(IFn<T> fn);

	boolean compareAndSet(Comparator<T> comparator, T current, T next);

	T getValue();

}
