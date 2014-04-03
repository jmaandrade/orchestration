package pt.jma.orchestration.context;

import java.lang.reflect.Proxy;

import pt.jma.common.ReflectionUtil;
import pt.jma.common.collection.IReduceProcessor;
import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.adapter.IServiceInvocation;
import pt.jma.orchestration.adapter.interceptor.AbstractServiceInterceptor;
import pt.jma.orchestration.context.config.InterceptorConfigType;
import pt.jma.orchestration.context.config.InterceptorType;
import pt.jma.orchestration.util.PropertiesUtil;

public class InterceptorProcessor implements IReduceProcessor<InterceptorType, IServiceInvocation> {

	IActivity activity;

	public InterceptorProcessor(IActivity activity) {
		super();
		this.activity = activity;
	}

	public IServiceInvocation execute(InterceptorType instance, IServiceInvocation serviceInvocationInstance) throws Throwable {

		String interceptorClassName = this.activity.getSettings().getActivityContext().getInterceptors().get(instance.getName()).getClazz();

		AbstractServiceInterceptor handler = ReflectionUtil.getInstance(interceptorClassName);

		InterceptorConfigType interceptorConfigType = this.activity.getSettings().getActivityContext().getInterceptors()
				.get(instance.getName());

		handler.getPropertiesMap().putAll(PropertiesUtil.getPropertiesMap(interceptorConfigType.getProperties()));
		handler.getPropertiesMap().putAll(PropertiesUtil.getPropertiesMap(instance.getProperties()));

		handler.setServiceInvocation(serviceInvocationInstance);

		serviceInvocationInstance = (IServiceInvocation) Proxy.newProxyInstance(((IServiceInvocation) serviceInvocationInstance).getClass()
				.getClassLoader(), new Class[] { IServiceInvocation.class }, handler);

		return serviceInvocationInstance;

	}

}
