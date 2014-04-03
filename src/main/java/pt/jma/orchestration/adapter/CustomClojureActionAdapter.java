package pt.jma.orchestration.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import pt.jma.common.collection.CollectionUtil;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.Response;
import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;
import clojure.lang.RT;

public class CustomClojureActionAdapter extends AbstractClojureServiceInvocationImpl implements IServiceInvocation {

	Map<Object, Serializable> map = null;

	public IResponse invoke(IRequest request) throws Exception {

		try {

			map = new HashMap<Object, Serializable>();

			CollectionUtil.map(super.getActionType().getBinds().getInputBind(), new BindRequestToClojureProcessor(map, request));

			IPersistentMap result = (IPersistentMap) this.getMethod().invoke(this.getTargetInstance(), PersistentHashMap.create(map));

			IResponse response = new Response();

			CollectionUtil.map(super.getActionType().getBinds().getOutputBind(), new BindClojureToResponseProcessor(result, response));

			Object outcome = result.valAt(RT.var("clojure.core", "keyword").invoke("outcome"));

			response.setOutcome(outcome instanceof String ? outcome.toString() : "success");

			return response;

		} catch (Throwable ex) {
			throw new Exception(ex);
		}

	}
}
