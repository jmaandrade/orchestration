package pt.jma.orchestration.exception;

public class InvalidStartException extends OrchestrationException {

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
