package pt.jma.orchestration.service;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.exception.OrchestrationException;

public class ServiceImpl extends AbstractService implements IService {

	public ServiceImpl(IActivity activity, ActionType actionType) throws OrchestrationException {
		super(activity, actionType);
	}

}
