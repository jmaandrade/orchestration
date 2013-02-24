package pt.jma.orchestration.exception;

public class OutcomeNotFoundException extends OrchestrationException {

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
