//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.29 at 07:01:16 PM WEST 
//

package pt.jma.orchestration.activity.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TransitionType  implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2980067719101452563L;

	@XmlAttribute(required = false)
	protected String current;

	@XmlAttribute(required = true)
	protected String next;

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	@XmlElement(name = "bind", type = BindType.class, required = true, namespace = "urn:pt.jma.orchestration.activity")
	protected List<BindType> binds;

	public List<BindType> getBinds() {
		if (binds == null) {
			binds = new ArrayList<BindType>();
		}
		return this.binds;
	}

}
