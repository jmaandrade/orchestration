package pt.jma.orchestration;

import java.util.Map;
import java.util.UUID;

import pt.jma.common.IMapUtil;

public interface IActivity {

	UUID getUUID();

	Map<String, IMapUtil> getScope();

	IResponse invoke(IRequest request) throws Throwable;

	IActivitySettings getSettings() throws Exception;

}
