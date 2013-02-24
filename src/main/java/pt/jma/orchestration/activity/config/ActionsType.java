package pt.jma.orchestration.activity.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "actionList" })
public class ActionsType {

	@XmlAttribute(required = false)
	protected String start;

	@XmlAttribute(required = false)
	protected String event;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	@XmlElement(name = "action", type = ActionType.class, namespace = "urn:pt.jma.orchestration.activity")
	protected List<ActionType> actionList;

	public List<ActionType> getActionlist() {
		if (actionList == null) {
			actionList = new ArrayList<ActionType>();
		}
		return this.actionList;
	}
}
