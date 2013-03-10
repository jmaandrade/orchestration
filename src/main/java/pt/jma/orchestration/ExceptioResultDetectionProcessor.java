package pt.jma.orchestration;

import pt.jma.common.collection.IReduceProcessor;
import pt.jma.orchestration.result.config.ResultType;

public class ExceptioResultDetectionProcessor implements IReduceProcessor<String, ResultType> {
	IActivity activity;
	Throwable t;

	public ExceptioResultDetectionProcessor(IActivity activity, Throwable t) {
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
