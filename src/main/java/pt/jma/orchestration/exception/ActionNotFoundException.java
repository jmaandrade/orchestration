package pt.jma.orchestration.exception;

import java.io.Serializable;

public class ActionNotFoundException extends OrchestrationException implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2977426857270236096L;

	public ActionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionNotFoundException(String message) {
		super(message);
	}

	public ActionNotFoundException(Throwable cause) {
		super(cause);
	}

}
