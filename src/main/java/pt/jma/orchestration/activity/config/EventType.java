package pt.jma.orchestration.activity.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class EventType {

	@XmlAttribute(required = true)
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "state", type = StateType.class, namespace = "urn:pt.jma.orchestration.activity")
	protected List<StateType> states;

	public List<StateType> getStates() {
		if (states == null) {
			states = new ArrayList<StateType>();
		}
		return this.states;
	}

	@XmlElement(name = "bind", type = BindType.class, required = false, namespace = "urn:pt.jma.orchestration.activity")
	protected List<BindType> binds;

	public List<BindType> getBinds() {
		if (binds == null) {
			binds = new ArrayList<BindType>();
		}
		return this.binds;
	}

}
