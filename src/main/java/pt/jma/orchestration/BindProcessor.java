package pt.jma.orchestration;

import java.io.Serializable;
import java.util.Map;

import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.exception.InvalidScopeException;

public class BindProcessor implements IMapProcessor<BindType> {

	IActivity activity;
	Map<String, String> scopesFromMap;

	public BindProcessor(IActivity activity, Map<String, String> scopesFromMap, Map<String, String> scopesToMap) {
		super();
		this.activity = activity;
		this.scopesFromMap = scopesFromMap;
		this.scopesToMap = scopesToMap;
	}

	Map<String, String> scopesToMap;

	@Override
	public boolean execute(BindType bindType) throws Throwable {
		String scopeFromKey = "";

		if (bindType.getScopeFrom() != null)
			scopeFromKey = bindType.getScopeFrom();

		if (!scopesFromMap.containsKey(scopeFromKey))
			throw new InvalidScopeException(scopeFromKey);

		String scopeToKey = "";

		if (bindType.getScopeTo() != null)
			scopeToKey = bindType.getScopeTo();

		if (!scopesToMap.containsKey(scopeToKey))
			throw new InvalidScopeException(scopeToKey);

		this.bindScope(bindType, scopesFromMap.get(scopeFromKey), scopesToMap.get(scopeToKey));
		return true;
	}

	private void bindScope(BindType bindType, String scopeFrom, String scopeTo) throws Exception {

		synchronized (this.activity.getScope().get(scopeFrom)) {
			synchronized (this.activity.getScope().get(scopeTo)) {

				if (this.activity.getScope().get(scopeFrom).containsKey(bindType.getFrom())) {

					Serializable value = (Serializable) this.activity.getScope().get(scopeFrom).get(bindType.getFrom());

					if (bindType.getConverter() != null) {
						value = (Serializable) ActivityImpl.getConverter(this.activity.getSettings(), bindType)
								.convert(Object.class, value);
					}

					this.activity.getScope().get(scopeTo).put(bindType.getTo(), value);
				}

			}
		}
	}

}