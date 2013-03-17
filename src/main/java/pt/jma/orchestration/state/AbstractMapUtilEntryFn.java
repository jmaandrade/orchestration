package pt.jma.orchestration.state;

import pt.jma.common.IMapUtil;

public abstract class AbstractMapUtilEntryFn<T> implements IFn<T> {

	public IMapUtil getMap() {
		return map;
	}

	public String getKey() {
		return key;
	}

	IMapUtil map;
	String key;

	public AbstractMapUtilEntryFn(IMapUtil map, String key) throws Throwable {
		super();
		this.map = map;
		this.key = key;
		if (!this.map.containsKey(key))
			this.map.put(key, "");
	}

	abstract public T call() throws Throwable;

}