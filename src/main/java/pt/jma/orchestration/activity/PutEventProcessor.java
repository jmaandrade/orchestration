package pt.jma.orchestration.activity;

import java.io.Serializable;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.EventType;

public class PutEventProcessor implements IMapProcessor<EventType>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9076567557760931931L;
	IActivitySettings activitySettings;

	public PutEventProcessor(IActivitySettings activitySettings) {
		super();
		this.activitySettings = activitySettings;
	}

	public boolean execute(EventType eventType) throws Throwable {

		if (activitySettings.getEventsMap().containsKey(eventType.getName())) {
			activitySettings.getEventsMap().get(eventType.getName()).getBindsAndStates().addAll(eventType.getBindsAndStates());
		} else
			activitySettings.getEventsMap().put(eventType.getName(), eventType);

		return true;

	}

}
