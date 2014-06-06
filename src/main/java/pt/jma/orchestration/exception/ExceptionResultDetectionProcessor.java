package pt.jma.orchestration.exception;

import java.io.Serializable;

import pt.jma.common.collection.IReduceProcessor;
import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.result.config.ResultType;

public class ExceptionResultDetectionProcessor implements IReduceProcessor<String, ResultType> , Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5076053311895934182L;
	IActivity activity;
	Throwable t;

	public ExceptionResultDetectionProcessor(IActivity activity, Throwable t) {
		super();
		this.activity = activity;
		this.t = t;
	}

	public ResultType execute(String key, ResultType result) throws Throwable {

		try {
			Throwable ex = t;
			while (ex != null) {
				if (Class.forName(key).isInstance(ex)) {
					return activity.getSettings().getResultsMap().get("exception").get(key);
				}

				ex = ex.getCause();
			}

		} catch (ClassNotFoundException classNotFound) {
		}

		return result;
	}

}
