package pt.jma.orchestration;

import java.util.UUID;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;

public class Request extends MapUtil implements IRequest {

	IMapUtil context = new MapUtil();

	public IMapUtil getContext() throws Exception {

		return this.context;
	}

	@Override
	public String getStartAction() throws Exception {
		return (String) this.getContext().get("start-action");
	}

	@Override
	public void setStartAction(String startAction) throws Exception {
		this.getContext().put("start-action", startAction);

	}

	@Override
	public void setUUID(UUID uuid) throws Exception {
		this.getContext().put("uuid", uuid);

	}

	@Override
	public UUID getUUID() throws Exception {

		return (UUID) this.getContext().get("uuid");
	}

	public Request() throws Exception {
		super();
		this.setUUID(UUID.randomUUID());
		this.setStartAction("");
	}
}
