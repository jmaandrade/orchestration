package pt.jma.orchestration.converters;

import java.io.Serializable;

public class GetFirstStringValue extends AbstractConverter implements IConverter<String> {

	public String convert(Serializable arg1) throws Exception {
		String[] values = (String[]) arg1;

		return values.length > 0 ? values[0] : null;
	}

}
