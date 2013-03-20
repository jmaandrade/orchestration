package pt.jma.orchestration;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.EventType;

public class PutEventProcessor implements IMapProcessor<EventType> {

	IActivitySettings activitySettings;

	public PutEventProcessor(IActivitySettings activitySettings) {
		super();
		this.activitySettings = activitySettings;
	}

	public boolean execute(EventType eventType) throws Throwable {
		activitySettings.getEventsMap().put(eventType.getName(), eventType);
		return true;

	}

}
