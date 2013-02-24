package pt.jma.orchestration.exception;

public class EventNotFoundException extends OrchestrationException {

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
