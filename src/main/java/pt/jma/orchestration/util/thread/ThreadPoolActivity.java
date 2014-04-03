package pt.jma.orchestration.util.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;

public class ThreadPoolActivity {

	IThreadActivityCaller caller = null;
	List<ThreadActivity> list = new ArrayList<ThreadActivity>();
	ExecutorService executor = null;

	public ThreadPoolActivity(ExecutorService executor) {
		super();
		this.executor = executor;
		this.caller = new ThreadPoolActivityCaller(this);
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public IThreadActivityCaller getCaller() {
		return caller;
	}

	public ThreadActivity add(IActivity activity, IRequest request) {

		ThreadActivity thread = new ThreadActivity(activity, request, this.getCaller());
		this.list.add(thread);
		this.executor.execute(thread);
		return thread;
	}

	public List<ThreadActivity> getList() {
		return list;
	}

	public void wait4AllResponses(int timeout) throws InterruptedException {

		synchronized (caller) {
			this.caller.wait(timeout);
		}

	}

}
