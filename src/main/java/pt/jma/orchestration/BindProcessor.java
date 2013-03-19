package pt.jma.orchestration;

import java.io.Serializable;
import java.util.Map;

import pt.jma.common.IMapUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.activity.config.BindType;
import pt.jma.orchestration.context.IConverter;
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

	private void bindScope(BindType bindType, String scopeFrom, String scopeTo) throws Throwable {

		IMapUtil mapFrom = this.activity.getScope().get(scopeFrom).getValue();

		if (mapFrom.containsKey(bindType.getFrom())) {

			Serializable value = (Serializable) mapFrom.get(bindType.getFrom());

			IConverter converter = (bindType.getConverter() != null ? ActivityImpl.getConverter(this.activity.getSettings(), bindType)
					: null);

			this.activity.getScope().get(scopeTo).swapEntry(new BindFn(bindType.getTo(), value, converter));

		}

	}

}
