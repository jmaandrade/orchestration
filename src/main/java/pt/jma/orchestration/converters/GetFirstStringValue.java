package pt.jma.orchestration.converters;

import java.io.Serializable;

public class GetFirstStringValue extends AbstractConverter implements IConverter<String> , Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6458771220214252745L;

	public String convert(Serializable arg1) throws Exception {
		String[] values = (String[]) arg1;

		return values.length > 0 ? values[0] : null;
	}

}
