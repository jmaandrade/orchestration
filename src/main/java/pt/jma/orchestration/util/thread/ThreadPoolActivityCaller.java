package pt.jma.orchestration.util.thread;

public class ThreadPoolActivityCaller implements IThreadActivityCaller {

	ThreadPoolActivity fixedThreadActivityPool = null;

	public ThreadPoolActivityCaller(ThreadPoolActivity fixedThreadActivityPool) {
		super();
		this.fixedThreadActivityPool = fixedThreadActivityPool;
	}

	public boolean notifyThreadEnd() {
		return this.fixedThreadActivityPool.getExecutor().isTerminated();
	}

}
