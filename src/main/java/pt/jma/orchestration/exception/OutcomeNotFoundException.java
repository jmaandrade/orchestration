package pt.jma.orchestration.exception;

import java.io.Serializable;

public class OutcomeNotFoundException extends OrchestrationException  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2355179396803016560L;

	public OutcomeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public OutcomeNotFoundException(String message) {
		super(message);

	}

	public OutcomeNotFoundException(Throwable cause) {
		super(cause);
	}

}
