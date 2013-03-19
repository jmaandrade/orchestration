package pt.jma.orchestration.test;

import pt.jma.common.IMapUtil;
import pt.jma.common.atomic.AbstractMapUtilEntryFn;

public class ConcatWithUpperCaseFn extends AbstractMapUtilEntryFn<String> {

	String value;
	IMapUtil map;

	public ConcatWithUpperCaseFn(IMapUtil map, String key, String value) throws Throwable {
		super(key);
		this.map = map;
		this.value = value;

	}

	public IMapUtil getMap() {
		return map;
	}

	public void setMap(IMapUtil map) {
		this.map = map;
	}

	@Override
	public String call() throws Throwable {

		return (this.getMap().containsKey(this.getKey()) ? this.getMap().get(this.getKey()) : "").toString().concat(value).toUpperCase();
	}

}
