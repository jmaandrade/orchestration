package pt.jma.orchestration.converters;

import java.io.Serializable;

public class String2BooleanConverter extends AbstractConverter implements IConverter<Boolean> {

	public Boolean convert(Serializable arg1) {
		return Boolean.parseBoolean(arg1.toString());
	}

}
