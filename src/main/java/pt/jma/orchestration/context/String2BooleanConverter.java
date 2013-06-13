package pt.jma.orchestration.context;

import java.io.Serializable;

public class String2BooleanConverter implements IConverter<Boolean> {

	public Boolean convert(Serializable arg1) {
		return Boolean.parseBoolean(arg1.toString());
	}

}
