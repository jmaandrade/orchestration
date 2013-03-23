package pt.jma.orchestration.adapter;

import java.io.Serializable;
import java.util.Map;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.IRequest;
import pt.jma.orchestration.activity.config.BindType;
import clojure.lang.RT;

public class BindRequestToClojureProcessor implements IMapProcessor<BindType> {

	Map<Object, Serializable> map;
	IRequest request;

	public BindRequestToClojureProcessor(Map<Object, Serializable> map, IRequest request) {
		super();
		this.map = map;
		this.request = request;
	}

	public boolean execute(BindType instance) throws Throwable {
		if (instance.getScopeTo() == null)
			map.put(RT.var("clojure.core", "keyword").invoke(instance.getFrom()), this.request.get(instance.getTo()));
		return true;
	}

}
