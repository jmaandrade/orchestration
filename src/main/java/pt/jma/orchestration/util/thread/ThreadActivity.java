package pt.jma.orchestration.util.thread;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;

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


	   synchronized (this.caller) {
		   
		try {
			this.response = null;
			this.ex = null;
			this.response = this.instance.invoke(this.request);
			

		} catch (Throwable ex) {
			this.ex = ex;
			
		} finally {
		    
			if (this.caller.notifyThreadEnd()) {
		    	    this.caller.notifyAll();
			    }
		}			
	   }


	}

	public Throwable getEx() {
		return ex;
	}

	public void setEx(Throwable ex) {
		this.ex = ex;
	}
}
