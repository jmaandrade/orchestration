package pt.jma.orchestration.activity.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ForwardType {

	@XmlAttribute
	protected String outcome;
	@XmlAttribute
	protected String action;

	@XmlAttribute(required = false)
	protected String event;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * Gets the value of the outcome property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOutcome() {
		return outcome;
	}

	/**
	 * Sets the value of the outcome property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOutcome(String value) {
		this.outcome = value;
	}

	/**
	 * Gets the value of the action property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the value of the action property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAction(String value) {
		this.action = value;
	}

}
