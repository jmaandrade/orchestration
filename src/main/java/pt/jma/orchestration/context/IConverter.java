package pt.jma.orchestration.context;

import java.io.Serializable;

public interface IConverter {

	public Serializable convert(Class arg0, Serializable arg1);

}
