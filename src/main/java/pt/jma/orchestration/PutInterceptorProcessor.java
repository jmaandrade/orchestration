package pt.jma.orchestration;

import java.util.Map;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.context.config.InterceptorConfigType;

public class PutInterceptorProcessor implements IMapProcessor<InterceptorConfigType> {

	Map<String, InterceptorConfigType> interceptors;

	public PutInterceptorProcessor(Map<String, InterceptorConfigType> interceptors) {
		super();
		this.interceptors = interceptors;
	}

	@Override
	public boolean execute(InterceptorConfigType instance) throws Throwable {

		interceptors.put(instance.getName(), instance);

		return true;

	}

}
