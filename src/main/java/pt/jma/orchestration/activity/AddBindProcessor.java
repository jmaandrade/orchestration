package pt.jma.orchestration.activity;

import java.io.Serializable;
import java.util.List;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.BindType;

public class AddBindProcessor implements IMapProcessor<BindType>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 933622203873832658L;
	List<BindType> binds;

	public AddBindProcessor(List<BindType> binds) {
		super();
		this.binds = binds;
	}

	public boolean execute(BindType bindType) throws Throwable {
		this.binds.add(bindType);
		return true;

	}

}
