package pt.jma.orchestration.exception;

import java.io.Serializable;

public class InvalidStartException extends OrchestrationException  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4942881064774272641L;

	public InvalidStartException() {

	}

	public InvalidStartException(String message, Throwable cause) {
		super(message, cause);

	}

	public InvalidStartException(String message) {
		super(message);

	}

	public InvalidStartException(Throwable cause) {
		super(cause);

	}

}
