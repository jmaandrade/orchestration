package pt.jma.orchestration.context;

import java.io.Serializable;
import java.util.Map;

import pt.jma.common.IMapUtil;
import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.context.config.AdapterConfigType;
import pt.jma.orchestration.context.config.ContextType;
import pt.jma.orchestration.context.config.ConverterType;
import pt.jma.orchestration.context.config.ServiceType;
import pt.jma.orchestration.converters.IConverter;
import pt.jma.orchestration.exception.OrchestrationException;
import pt.jma.orchestration.result.config.ResultType;
import pt.jma.orchestration.service.IService;
import pt.jma.orchestration.util.IConfigurableInheritedElement;

public interface IActivityContext extends IConfigurableInheritedElement<ContextType> {

	IMapUtil getState();

	Map<String, AdapterConfigType> getAdapters() throws Exception;

	Map<String, ConverterType> getConverters() throws Exception;

	Map<String, ServiceType> getServices() throws Exception;

	Map<String, Map<String, ResultType>> getResults() throws Exception;

	IActivity lookup(String name) throws OrchestrationException, Exception;

	IConverter<Serializable> getConverter(String name) throws Exception;

	IService getService(String name) throws Throwable;

}
