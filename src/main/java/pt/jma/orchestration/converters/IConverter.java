package pt.jma.orchestration.converters;

import java.io.Serializable;

public interface IConverter<t extends Serializable> {

	t convert(Serializable arg1) throws Exception;

}
