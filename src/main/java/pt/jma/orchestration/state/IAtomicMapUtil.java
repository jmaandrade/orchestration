package pt.jma.orchestration.state;

import java.io.Serializable;
import java.util.Comparator;

import pt.jma.common.IMapUtil;

public interface IAtomicMapUtil {

	Serializable swapEntry(AbstractMapUtilEntryFn fn) throws Throwable;

	<T extends Serializable> boolean compareAndSet(String key, Comparator<T> comparator, T current, T next) throws Throwable;

	IMapUtil getValue();

}
