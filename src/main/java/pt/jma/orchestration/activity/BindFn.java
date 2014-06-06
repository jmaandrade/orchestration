package pt.jma.orchestration.activity;

import java.io.Serializable;

import pt.jma.common.atomic.AbstractMapUtilEntryFn;
import pt.jma.orchestration.converters.IConverter;

public class BindFn extends AbstractMapUtilEntryFn<Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -925550960992312878L;

	Serializable value;
	IConverter<Serializable> converter;
	IActivitySettings settings;

	public BindFn(String key, Serializable value, IConverter converter) throws Throwable {
		super(key);
		this.value = value;
		this.converter = converter;

	}

	@Override
	public Serializable call() throws Throwable {

		return (converter != null ? converter.convert(value) : value);

	}

}
