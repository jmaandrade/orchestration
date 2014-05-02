package pt.jma.orchestration.service;

import java.util.Map;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.exception.OrchestrationException;

public interface IService {

	ActionType getActionType();

	Map<String, String> getProperties();

	IResponse invoke(IRequest request) throws OrchestrationException;

	IAdapter getAdapter() throws OrchestrationException;

}
