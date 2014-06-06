package pt.jma.orchestration.context;

import java.io.Serializable;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.AbstractActivityProcessor;
import pt.jma.orchestration.activity.IActivitySettings;

public class PutActivityPropertyProcessor extends AbstractActivityProcessor implements IMapProcessor<PropertyType>  , Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7470838471025445571L;

	public PutActivityPropertyProcessor(IActivitySettings activitySettings) {
		super(activitySettings);

	}

	public boolean execute(PropertyType propertyType) throws Throwable {
		activitySettings.getProperties().put(propertyType.getName(), propertyType.getValue());
		return true;

	}

}
