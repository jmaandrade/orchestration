package pt.jma.orchestration.context;

import java.io.Serializable;

public class GetFirstStringValue implements IConverter {

	public Serializable convert(Class arg0, Serializable arg1) {
		String[] values = (String[]) arg1;

		return values.length > 0 ? values[0] : null;
	}

}
