package pt.jma.orchestration;

import java.util.UUID;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;

public class Response extends MapUtil implements IResponse {

	IMapUtil context = new MapUtil();

	public IMapUtil getContext() throws Exception {

		return this.context;
	}

	public String getOutcome() throws Exception {
		return (String) this.getContext().get("outcome");
	}

	public void setOutcome(String outcome) throws Exception {
		this.getContext().put("outcome", outcome);

	}

	public void setUUID(UUID uuid) throws Exception {
		this.getContext().put("uuid", uuid);

	}

	public UUID getUUID() throws Exception {

		return (UUID) this.getContext().get("uuid");
	}

	public Response() throws Exception {
		super();
		this.setUUID(UUID.randomUUID());
		this.setOutcome("sucess");
	}

}
