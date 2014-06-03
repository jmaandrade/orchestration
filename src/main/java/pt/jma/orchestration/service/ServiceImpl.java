package pt.jma.orchestration.service;

import pt.jma.common.ReflectionUtil;
import pt.jma.orchestration.adapter.IAdapter;
import pt.jma.orchestration.context.config.AdapterConfigType;

public class ServiceImpl extends AbstractService implements IService {

	@Override
	public IAdapter getNewAdapterInstance(AdapterConfigType adapterConfigType) throws Throwable {
		
		IAdapter instance = (IAdapter) ReflectionUtil.getInstance(adapterConfigType.getClazz());
		instance.setService((IService) this);
		instance.setConfig(adapterConfigType);

		return instance;
	}

}
