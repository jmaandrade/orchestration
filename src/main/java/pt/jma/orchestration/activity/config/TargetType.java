package pt.jma.orchestration.activity.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class TargetType {

	@XmlAttribute(name = "class")
	protected String clazz;
	@XmlAttribute
	protected String method;

	/**
	 * Gets the value of the clazz property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * Sets the value of the clazz property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClazz(String value) {
		this.clazz = value;
	}

	/**
	 * Gets the value of the method property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the value of the method property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMethod(String value) {
		this.method = value;
	}

}
