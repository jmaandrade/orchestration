package pt.jma.orchestration.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.result.config.ResultType;

public class PutResultProcessor implements IMapProcessor<ResultType>  , Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1008329270483226211L;
	
	Map<String, Map<String, ResultType>> results;

	public PutResultProcessor(Map<String, Map<String, ResultType>> results) {
		super();
		this.results = results;
	}

	public boolean execute(ResultType instance) throws Throwable {

		if (!results.containsKey(instance.getType())) {
			results.put(instance.getType(), new HashMap<String, ResultType>());
		}
		results.get(instance.getType()).put(instance.getName(), instance);

		return true;

	}

}
