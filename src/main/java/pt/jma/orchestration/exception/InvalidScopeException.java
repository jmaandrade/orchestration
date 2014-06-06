package pt.jma.orchestration.exception;

import java.io.Serializable;

public class InvalidScopeException extends OrchestrationException  implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7546456613056415835L;

	public InvalidScopeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidScopeException(String message) {
		super(message);
	}

	public InvalidScopeException(Throwable cause) {
		super(cause);
	}

}
