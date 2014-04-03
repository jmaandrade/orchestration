package pt.jma.orchestration.activity;

import java.util.ArrayList;
import java.util.List;

import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.activity.config.StateType;

public class ActivityEventProcessor implements IMapProcessor<Object> {

	IActivity activity;
	IMapProcessor<BindType> bindProcessor = null;

	public ActivityEventProcessor(IActivity activity) {
		super();
		this.activity = activity;
	}

	public IMapProcessor<BindType> getBindProcessor() {
		return bindProcessor;
	}

	public void setBindProcessor(IMapProcessor<BindType> bindProcessor) {
		this.bindProcessor = bindProcessor;
	}

	public boolean execute(Object instance) throws Throwable {

		if (instance instanceof StateType) {
			StateType stateType = (StateType) instance;
			CollectionUtil.map(stateType.getTransitions(), new TransitionProcessor(this.activity, stateType));
		} else {
			List<BindType> list = new ArrayList<BindType>();
			list.add((BindType) instance);
			CollectionUtil.map(list, this.bindProcessor);
		}
		return true;

	}

}
