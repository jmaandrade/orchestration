package pt.jma.orchestration.exception;

import java.io.Serializable;

public class EventNotFoundException extends OrchestrationException implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2430619427543692898L;

	public EventNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventNotFoundException(String message) {
		super(message);

	}

	public EventNotFoundException(Throwable cause) {
		super(cause);
	}

}
