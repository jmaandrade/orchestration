package pt.jma.orchestration.activity;

import java.util.Map;
import java.util.Observer;
import java.util.UUID;

import pt.jma.common.atomic.IAtomicMapUtil;
import pt.jma.orchestration.result.IResultFn;
import pt.jma.orchestration.util.thread.IThreadActivityCaller;
import pt.jma.orchestration.util.thread.ThreadActivity;

public interface IActivity {

	UUID getUUID();

	Map<String, IAtomicMapUtil> getScope();

	IResponse invoke(IRequest request) throws Throwable;

	ThreadActivity invokeAsynchr(IRequest request) throws Throwable;

	ThreadActivity invokeAsynchr(IRequest request, IThreadActivityCaller caller) throws Throwable;

	IActivitySettings getSettings() throws Exception;

	void setSettings(IActivitySettings settings) throws Exception;

	void addEventObserver(IEventActivityObserver observer);

	void addObserver(Observer observer);

	Map<String, IResultFn> getTargetResultFnMap();

}
