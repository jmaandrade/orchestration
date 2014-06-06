package pt.jma.orchestration.activity.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class StateType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8329953568089223809L;
	@XmlAttribute
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "transition", type = TransitionType.class, namespace = "urn:pt.jma.orchestration.activity")
	protected List<TransitionType> transitions;

	public List<TransitionType> getTransitions() {
		if (transitions == null) {
			transitions = new ArrayList<TransitionType>();
		}
		return this.transitions;
	}

}
