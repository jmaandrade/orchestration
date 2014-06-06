package pt.jma.orchestration.exception;

import java.io.Serializable;

public class OrchestrationException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4943396665354120043L;

	public OrchestrationException() {
		super();

	}

	public OrchestrationException(String message, Throwable cause) {
		super(message, cause);

	}

	public OrchestrationException(String message) {
		super(message);

	}

	public OrchestrationException(Throwable cause) {
		super(cause);

	}

}
