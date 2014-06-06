package pt.jma.orchestration.activity.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
public class EventType  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3632054556410765496L;
	
	@XmlAttribute(required = true)
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElements({ @XmlElement(name = "state", type = StateType.class, namespace = "urn:pt.jma.orchestration.activity"),
			@XmlElement(name = "bind", type = BindType.class, required = false, namespace = "urn:pt.jma.orchestration.activity") })
	protected List<Object> bindsAndStates;

	public List<Object> getBindsAndStates() {
		if (bindsAndStates == null) {
			bindsAndStates = new ArrayList<Object>();
		}
		return this.bindsAndStates;
	}

}
