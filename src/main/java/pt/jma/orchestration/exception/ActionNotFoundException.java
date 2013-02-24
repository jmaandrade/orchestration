package pt.jma.orchestration.exception;

public class ActionNotFoundException extends OrchestrationException {

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
