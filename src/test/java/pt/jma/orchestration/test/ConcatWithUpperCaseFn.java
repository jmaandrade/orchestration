package pt.jma.orchestration.test;

import pt.jma.common.IMapUtil;
import pt.jma.orchestration.state.AbstractMapUtilEntryFn;

public class ConcatWithUpperCaseFn extends AbstractMapUtilEntryFn<String> {

	String value;

	public ConcatWithUpperCaseFn(IMapUtil map, String key, String value) throws Throwable {
		super(map, key);
		this.value = value;

	}

	@Override
	public String call() throws Throwable {

		return (this.getMap().containsKey(this.getKey()) ? this.getMap().get(this.getKey()) : "").toString().concat(value).toUpperCase();
	}

}
