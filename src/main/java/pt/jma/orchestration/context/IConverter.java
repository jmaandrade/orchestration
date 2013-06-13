package pt.jma.orchestration.context;

import java.io.Serializable;

public interface IConverter<t extends Serializable> {

	t convert(Serializable arg1) throws Exception;

}
