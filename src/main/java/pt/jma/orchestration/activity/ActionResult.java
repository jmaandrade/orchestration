package pt.jma.orchestration.activity;

public final class ActionResult {

	Throwable error;
	String outcome;

	public ActionResult() {
		super();

	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

}
