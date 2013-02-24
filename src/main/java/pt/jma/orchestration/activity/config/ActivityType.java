package pt.jma.orchestration.activity.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pt.jma.orchestration.PropertyType;
import pt.jma.orchestration.result.config.ResultType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "activity", namespace = "urn:pt.jma.orchestration.activity")
@XmlType(propOrder = { "properties", "actions", "results", "binds", "events" })
public class ActivityType {

	public BindsType getBinds() {
		return binds;
	}

	public void setBinds(BindsType binds) {
		this.binds = binds;
	}

	@XmlAttribute
	protected String parent;

	@XmlElement(required = false, namespace = "urn:pt.jma.orchestration.activity")
	protected BindsType binds;

	@XmlElementWrapper(name = "properties", namespace = "urn:pt.jma.orchestration.activity")
	@XmlElement(name = "property", type = PropertyType.class, namespace = "urn:pt.jma.orchestration.activity")
	protected List<PropertyType> properties;

	public List<PropertyType> getProperties() {
		if (properties == null) {
			properties = new ArrayList<PropertyType>();
		}
		return this.properties;
	}

	public ActionsType getActions() {

		if (actions == null) {
			actions = new ActionsType();
		}

		return actions;
	}

	public void setActions(ActionsType actions) {
		this.actions = actions;
	}

	@XmlElement(name = "actions", type = ActionsType.class, namespace = "urn:pt.jma.orchestration.activity")
	ActionsType actions;

	@XmlElementWrapper(name = "results", required = true, namespace = "urn:pt.jma.orchestration.activity")
	@XmlElement(name = "result", required = true, type = ResultType.class, namespace = "urn:pt.jma.orchestration.activity")
	List<ResultType> results;

	public List<ResultType> getResults() {
		return results;
	}

	public void setResults(List<ResultType> results) {
		this.results = results;
	}

	@XmlElementWrapper(name = "events", required = true, namespace = "urn:pt.jma.orchestration.activity")
	@XmlElement(name = "event", required = true, type = EventType.class, namespace = "urn:pt.jma.orchestration.activity")
	List<EventType> events;

	public List<EventType> getEvents() {
		return events;
	}

	public void setEvents(List<EventType> events) {
		this.events = events;
	}

	/**
	 * Gets the value of the parent property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * Sets the value of the parent property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setParent(String value) {
		this.parent = value;
	}

}
