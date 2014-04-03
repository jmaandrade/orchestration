package pt.jma.orchestration.context;

import java.util.Map;

import pt.jma.common.IMapUtil;
import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.context.config.ConverterType;
import pt.jma.orchestration.context.config.InterceptorConfigType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.result.config.ResultType;

public interface IActivityContext {

	IMapUtil getState();

	Map<String, AdapterConfigType> getAdapters() throws Exception;

	Map<String, ConverterType> getConverters() throws Exception;

	Map<String, InterceptorConfigType> getInterceptors() throws Exception;

	Map<String, String> getProperties() throws Exception;

	Map<String, ServiceType> getServices() throws Exception;

	Map<String, Map<String, ResultType>> getResults() throws Exception;

	IActivity lookup(String name) throws OrchestrationException, Exception;

}
