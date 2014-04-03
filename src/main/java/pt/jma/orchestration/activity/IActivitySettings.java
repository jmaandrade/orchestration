package pt.jma.orchestration.activity;

import java.util.List;
import java.util.Map;

import pt.jma.common.IMapUtil;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.EventType;
import pt.jma.orchestration.context.IActivityContext;
import pt.jma.orchestration.result.config.ResultType;

public interface IActivitySettings {

	String getStartActivityEvent();

	void setStartActivityEvent(String startActivityEvent);

	IActivityContext getActivityContext();

	void setActivityContext(IActivityContext activityContext);

	String getName();

	void setName(String name);

	String getStart();

	void setStart(String start);

	Map<String, ActionType> getActionMap();

	IMapUtil getProperties();

	Map<String, Map<String, ResultType>> getResultsMap();

	List<BindType> getInputBinds();

	List<BindType> getOutputBinds();

	Map<String, EventType> getEventsMap();

}
