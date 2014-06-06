package pt.jma.orchestration.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.orchestration.context.PropertyType;

public abstract class AbstractConfigurableElement<T extends IConfigProperties> implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4293486752674956065L;

	public static IMapUtil getPropertiesMap(List<PropertyType> list) {

		IMapUtil instance = new MapUtil();
		try {
			if (list != null && !list.isEmpty()) {
				Iterator<PropertyType> iterator = list.iterator();

				while (iterator.hasNext()) {
					PropertyType propertyType = iterator.next();
					instance.put(propertyType.getName(), propertyType.getValue());
				}
			}
		} catch (Throwable ex) {

		}
		return instance;

	}

	T config;

	public T getConfig() {
		return config;
	}

	public void setConfig(T config) {
		this.config = config;
	}

	IMapUtil properties = null;

	public IMapUtil getProperties() {

		if (this.properties == null) {
			this.properties = AbstractConfigurableElement.getPropertiesMap(config == null ? null : config.getProperties());
		}

		return properties;
	}

}
