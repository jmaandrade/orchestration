package pt.jma.orchestration.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.orchestration.context.PropertyType;

public abstract class AbstractConfigurableInheritedElement<T extends IConfigProperties>  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3259528524878877973L;

	void bindProperties(List<PropertyType> list) {

		try {
			if (list != null && !list.isEmpty()) {
				Iterator<PropertyType> iterator = list.iterator();

				while (iterator.hasNext()) {
					PropertyType propertyType = iterator.next();
					properties.put(propertyType.getName(), propertyType.getValue());
				}
			}
		} catch (Throwable ex) {

		}

	}

	List<T> configList = new ArrayList<T>();

	public List<T> getConfigList() {
		return configList;
	}

	IMapUtil properties = null;

	public IMapUtil getProperties() {

		if (this.properties == null) {
			properties = new MapUtil();
			for (Iterator<T> iter = configList.iterator(); iter.hasNext();) {
				bindProperties(iter.next().getProperties());
			}
		}

		return properties;
	}
}
