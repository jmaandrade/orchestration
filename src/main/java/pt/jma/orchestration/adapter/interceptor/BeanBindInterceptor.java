package pt.jma.orchestration.adapter.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.jma.common.StringUtils;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.config.BindType;

public class BeanBindInterceptor extends AbstractServiceInterceptor {

	@Override
	public void initTarget() throws Exception {

	}

	@Override
	public void beforeInvoke(IRequest request) throws Exception {

		Iterator<BindType> iterator;

		if (super.getServiceInvocation().getActionType().getBinds().getInputBind() != null) {
			iterator = super.getServiceInvocation().getActionType().getBinds().getInputBind().iterator();

			while (iterator.hasNext()) {
				BindType bindType = iterator.next();

				if (bindType.getScopeTo() == null && request.containsKey(bindType.getTo())) {

					String methodName = String.format("set%s", StringUtils.toUpperFirstChar(bindType.getTo()));

					Method set = null;

					for (Type type : request.get(bindType.getTo()).getClass().getInterfaces()) {
						if (request.get(bindType.getFrom()) instanceof Map)
							set = super.getServiceInvocation().getTargetInstance().getClass()
									.getMethod(methodName, new Class[] { Map.class });
						else if (request.get(bindType.getTo()) instanceof List)
							set = super.getServiceInvocation().getTargetInstance().getClass()
									.getMethod(methodName, new Class[] { List.class });
						else {
							set = super.getServiceInvocation().getTargetInstance().getClass()
									.getMethod(methodName, new Class[] { request.get(bindType.getTo()).getClass() });
							break;
						}
					}
					set.invoke(super.getServiceInvocation().getTargetInstance(), request.get(bindType.getTo()));
				}
			}
		}

	}

	@Override
	public void afterInvoke(IResponse response) throws Exception {

		Iterator<BindType> iterator;

		if (super.getServiceInvocation().getActionType().getBinds().getOutputBind() != null) {

			iterator = super.getServiceInvocation().getActionType().getBinds().getOutputBind().iterator();

			while (iterator.hasNext()) {
				BindType bindType = iterator.next();

				if (bindType.getScopeFrom() == null) {

					String methodName = String.format("get%s", StringUtils.toUpperFirstChar(bindType.getFrom()));

					response.put(bindType.getFrom(),
							(Serializable) super.getServiceInvocation().getTargetInstance().getClass()
									.getMethod(methodName, new Class[] {}).invoke(super.getServiceInvocation().getTargetInstance()));
				}
			}

		}

	}

	@Override
	public void handleException(Exception ex) throws Exception {
		throw ex;

	}
}
