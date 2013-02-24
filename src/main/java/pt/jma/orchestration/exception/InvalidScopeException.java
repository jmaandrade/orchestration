package pt.jma.orchestration.exception;

public class InvalidScopeException extends OrchestrationException {

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
