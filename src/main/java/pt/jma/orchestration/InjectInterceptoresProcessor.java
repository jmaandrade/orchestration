package pt.jma.orchestration;

import java.lang.reflect.Proxy;

import pt.jma.common.ReflectionUtil;
import pt.jma.common.collection.IReduceProcessor;
import pt.jma.orchestration.context.config.InterceptorType;
import pt.jma.orchestration.util.PropertiesUtil;

public class InjectInterceptoresProcessor implements IReduceProcessor<InterceptorType, IActivity> {

	IActivityContext activityContext;

	public InjectInterceptoresProcessor(IActivityContext activityContext) {
		super();
		this.activityContext = activityContext;
	}

	@Override
	public IActivity execute(InterceptorType interceptor, IActivity result) throws Throwable {

		String interceptorClassName = activityContext.getInterceptors().get(interceptor.getName()).getClazz();

		AbstractActivityInvocationInterceptor handler = ReflectionUtil.getInstance(interceptorClassName);
		handler.setActivity(result);

		handler.getPropertiesMap().putAll(PropertiesUtil.getPropertiesMap(interceptor.getProperties()));

		result = (IActivity) Proxy.newProxyInstance(((IActivity) result).getClass().getClassLoader(), new Class[] { IActivity.class },
				handler);

		return result;
	}

}
