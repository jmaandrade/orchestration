//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.01 at 10:47:43 AM WEST 
//

package pt.jma.orchestration.result.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import pt.jma.orchestration.activity.config.BindType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResultType  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4226671053528084148L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	@XmlAttribute(required = true)
	protected String name;

	@XmlAttribute(required = true)
	protected String type;

	@XmlAttribute(required = true)
	protected String targetName;

	@XmlAttribute(required = true)
	protected String targetType;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@XmlAttribute(required = false)
	protected String event;

	@XmlElement(name = "bind", type = BindType.class, required = false, namespace = "urn:pt.jma.orchestration.activity")
	protected List<BindType> binds;

	public List<BindType> getBinds() {
		if (binds == null) {
			binds = new ArrayList<BindType>();
		}
		return this.binds;
	}

}
