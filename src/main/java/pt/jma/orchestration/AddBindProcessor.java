package pt.jma.orchestration;

import java.util.List;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.BindType;

public class AddBindProcessor implements IMapProcessor<BindType> {

	List<BindType> binds;

	public AddBindProcessor(List<BindType> binds) {
		super();
		this.binds = binds;
	}

	@Override
	public boolean execute(BindType bindType) throws Throwable {
		this.binds.add(bindType);
		return true;

	}

}
