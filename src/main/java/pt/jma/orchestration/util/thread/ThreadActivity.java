package pt.jma.orchestration.util.thread;

import pt.jma.orchestration.IActivity;
import pt.jma.orchestration.IRequest;
import pt.jma.orchestration.IResponse;

public class ThreadActivity extends Thread {

	IActivity instance;
	IRequest request;
	IResponse response = null;
	Throwable ex = null;
	IThreadActivityCaller caller = null;

	public void wait4Response(int timeout) {

		synchronized (this.caller) {

			try {
				this.caller.wait(timeout);

			} catch (Throwable ex) {
				this.ex = ex;
			}
		}
	}

	public IResponse getResponse() {
		return response;
	}

	public void setResponse(IResponse response) {
		this.response = response;
	}

	public ThreadActivity(IActivity instance, IRequest request, IThreadActivityCaller caller) {

		super();
		this.instance = instance;
		this.request = request;
		this.caller = caller;

	}

	public void run() {
		try {
			this.response = null;
			this.ex = null;
			this.response = this.instance.invoke(this.request);

			synchronized (this.caller) {

				if (this.caller.notifyThreadEnd()) {
					this.caller.notifyAll();
				}

			}

		} catch (Throwable ex) {
			this.ex = ex;
		}
	}

	public Throwable getEx() {
		return ex;
	}

	public void setEx(Throwable ex) {
		this.ex = ex;
	}
}
