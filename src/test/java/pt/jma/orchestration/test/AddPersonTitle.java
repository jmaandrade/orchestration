package pt.jma.orchestration.test;

import java.io.Serializable;

import pt.jma.orchestration.converters.AbstractConverter;
import pt.jma.orchestration.converters.IConverter;

public class AddPersonTitle extends AbstractConverter implements IConverter<String> {

	public String convert(Serializable arg1) throws Exception {
		System.out.println("convert: ".concat(arg1.toString()));
		return ((String) this.getProperties().get("title")).concat(" ").concat(arg1 == null ? "" : (String) arg1);
	}

}
