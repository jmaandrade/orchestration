package pt.jma.orchestration.activity;

import java.util.UUID;

import pt.jma.common.IMapUtil;

public interface IRequest extends IMapUtil {

	UUID getUUID() throws Exception;

	void setUUID(UUID uuid) throws Exception;

	String getStartAction() throws Exception;

	void setStartAction(String action) throws Exception;

	IMapUtil getContext() throws Exception;

}
