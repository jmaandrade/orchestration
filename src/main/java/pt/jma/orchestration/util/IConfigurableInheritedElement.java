package pt.jma.orchestration.util;

import java.util.List;

import pt.jma.common.IMapUtil;

public interface IConfigurableInheritedElement<T> {

	List<T> getConfigList();

	IMapUtil getProperties();

}
