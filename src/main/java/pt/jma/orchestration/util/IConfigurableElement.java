package pt.jma.orchestration.util;

import pt.jma.common.IMapUtil;

public interface IConfigurableElement<T> {

	T getConfig();

	void setConfig(T config);

	IMapUtil getProperties();

}
