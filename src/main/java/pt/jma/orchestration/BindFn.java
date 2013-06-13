package pt.jma.orchestration;

import java.io.Serializable;

import pt.jma.common.atomic.AbstractMapUtilEntryFn;
import pt.jma.orchestration.context.IConverter;

public class BindFn extends AbstractMapUtilEntryFn<Serializable> {

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

		if (value != null) {

			if (converter != null) {
				value = converter.convert(value);
			}
		}

		return value;

	}

}
