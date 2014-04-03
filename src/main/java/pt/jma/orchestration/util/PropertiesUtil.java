package pt.jma.orchestration.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.jma.orchestration.context.PropertyType;

public class PropertiesUtil {

	public static Map<String, String> getPropertiesMap(List<PropertyType> list) throws Exception {

		Map<String, String> instance = new HashMap<String, String>();

		if (list != null && !list.isEmpty()) {
			Iterator<PropertyType> iterator = list.iterator();

			while (iterator.hasNext()) {
				PropertyType propertyType = iterator.next();
				instance.put(propertyType.getName(), propertyType.getValue());
			}
		}

		return instance;

	}

}
