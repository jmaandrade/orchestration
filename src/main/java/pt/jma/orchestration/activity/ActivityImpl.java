package pt.jma.orchestration.activity;

import pt.jma.orchestration.service.IService;
import pt.jma.orchestration.service.ServiceImpl;

public class ActivityImpl extends AbstractActivity implements IActivity {

	public ActivityImpl(IActivitySettings settings) {
		super();
		this.settings = settings;

	}

	@Override
	IService getServiceInstance() throws Throwable {
		return new ServiceImpl();
	}

}
