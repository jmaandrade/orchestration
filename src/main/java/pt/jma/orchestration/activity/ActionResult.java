package pt.jma.orchestration.activity;

import java.io.Serializable;

public final class ActionResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7682689657333751651L;
	
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
