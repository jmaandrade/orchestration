package pt.jma.orchestration.service;

import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.activity.config.ActionType;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.context.IActivityContext;
import pt.jma.orchestration.context.config.AdapterConfigType;

public class ServiceImpl extends AbstractService implements IService {

	public ServiceImpl(IActivityContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IAdapter getNewAdapterInstance(AdapterConfigType adapterConfigType) throws Throwable {

		IAdapter instance = (IAdapter) ReflectionUtil.getInstance(adapterConfigType.getClazz());
		instance.setService((IService) this);
		instance.setConfig(adapterConfigType);

		return instance;
	}

	@Override
	public IServiceInvocation getNewInvocationInstance(ActionType actionType) throws Throwable {
		IServiceInvocation invocation = new ServiceInvocationImpl();
		invocation.setActionType(actionType);
		invocation.setService((IService) this);
		return invocation;
	}

}
