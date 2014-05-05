package pt.jma.orchestration.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.activity.config.ActivityType;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.EventType;
import pt.jma.orchestration.context.IActivityContext;
import pt.jma.orchestration.result.config.ResultType;
import pt.jma.orchestration.util.AbstractConfigurableInheritedElement;

public class ActivitySettings extends AbstractConfigurableInheritedElement<ActivityType> implements IActivitySettings {

	String startActivityEvent = null;

	public void setStartActivityEvent(String startActivityEvent) {
		this.startActivityEvent = startActivityEvent;
	}

	Map<String, ActionType> actionMap = new HashMap<String, ActionType>();

	IActivityContext activityContext;

	Map<String, EventType> eventsMap = new HashMap<String, EventType>();

	List<BindType> inputBinds = new ArrayList<BindType>();

	public Map<String, EventType> getEventsMap() {
		return eventsMap;
	}

	List<BindType> outputBinds = new ArrayList<BindType>();

	public List<BindType> getInputBinds() {
		return inputBinds;
	}

	public List<BindType> getOutputBinds() {
		return outputBinds;
	}

	String name;

	Map<String, Map<String, ResultType>> results = new HashMap<String, Map<String, ResultType>>();

	public Map<String, Map<String, ResultType>> getResultsMap() {
		return results;
	}

	String start = null;

	public IActivityContext getActivityContext() {
		return activityContext;
	}

	public void setActivityContext(IActivityContext activityContext) {
		this.activityContext = activityContext;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public Map<String, ActionType> getActionMap() {
		return actionMap;
	}

	public String getStartActivityEvent() {
		return startActivityEvent;
	}

}
