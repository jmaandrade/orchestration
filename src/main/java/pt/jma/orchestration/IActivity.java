package pt.jma.orchestration;

import java.util.Map;
import java.util.UUID;

import pt.jma.common.atomic.IAtomicMapUtil;
import pt.jma.orchestration.util.thread.ThreadActivity;

public interface IActivity {

	UUID getUUID();

	Map<String, IAtomicMapUtil> getScope();

	IResponse invoke(IRequest request) throws Throwable;

	ThreadActivity invokeAsynchr(IRequest request) throws Throwable;

	IActivitySettings getSettings() throws Exception;

}
