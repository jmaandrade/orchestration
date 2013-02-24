//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.29 at 06:51:07 PM WEST 
//

package pt.jma.orchestration.context.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import pt.jma.orchestration.PropertyType;

@XmlAccessorType(XmlAccessType.FIELD)
public class InterceptorType {

	@XmlElementWrapper(name = "properties", namespace = "urn:pt.jma.orchestration.context")
	@XmlElement(name = "property", type = PropertyType.class, namespace = "urn:pt.jma.orchestration.context")
	protected List<PropertyType> properties;

	public List<PropertyType> getProperties() {
		if (properties == null) {
			properties = new ArrayList<PropertyType>();
		}
		return this.properties;
	}

	@XmlAttribute
	protected String name;

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

}
