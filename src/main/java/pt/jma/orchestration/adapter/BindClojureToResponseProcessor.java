package pt.jma.orchestration.adapter;

import java.io.Serializable;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.IResponse;
import pt.jma.orchestration.activity.config.BindType;
import clojure.lang.IPersistentMap;
import clojure.lang.RT;

public class BindClojureToResponseProcessor implements IMapProcessor<BindType> {

	IPersistentMap result;
	IResponse response;

	public BindClojureToResponseProcessor(IPersistentMap result, IResponse response) {
		super();
		this.result = result;
		this.response = response;
	}

	public boolean execute(BindType instance) throws Throwable {
		if (instance.getScopeFrom() == null)
			response.put(instance.getFrom(), (Serializable) result.valAt(RT.var("clojure.core", "keyword").invoke(instance.getTo())));

		return true;
	}

}
