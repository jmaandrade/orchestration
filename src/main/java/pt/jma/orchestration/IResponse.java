package pt.jma.orchestration;

import java.util.UUID;

import pt.jma.common.IMapUtil;

public interface IResponse extends IMapUtil {

	void setUUID(UUID uuid) throws Exception;

	UUID getUUID() throws Exception;

	String getOutcome() throws Exception;

	void setOutcome(String outcome) throws Exception;

	IMapUtil getContext() throws Exception;

}
