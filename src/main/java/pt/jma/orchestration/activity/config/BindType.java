//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.01 at 10:47:43 AM WEST 
//

package pt.jma.orchestration.activity.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class BindType {

	@XmlAttribute(required = false)
	protected String scopeFrom;
	@XmlAttribute(required = false)
	protected String scopeTo;
	@XmlAttribute
	protected String from;
	@XmlAttribute
	protected String to;
	@XmlAttribute
	protected String converter;

	/**
	 * Gets the value of the from property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFrom() {
		return from;
	}

	public String getScopeFrom() {
		return scopeFrom;
	}

	public void setScopeFrom(String scopeFrom) {
		this.scopeFrom = scopeFrom;
	}

	public String getScopeTo() {
		return scopeTo;
	}

	public void setScopeTo(String scopeTo) {
		this.scopeTo = scopeTo;
	}

	/**
	 * Sets the value of the from property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFrom(String value) {
		this.from = value;
	}

	/**
	 * Gets the value of the to property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets the value of the to property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTo(String value) {
		this.to = value;
	}

	/**
	 * Gets the value of the converter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getConverter() {
		return converter;
	}

	/**
	 * Sets the value of the converter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setConverter(String value) {
		this.converter = value;
	}

}