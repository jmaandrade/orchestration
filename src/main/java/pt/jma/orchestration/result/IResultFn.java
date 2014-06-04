package pt.jma.orchestration.result;

import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.result.config.ResultType;

public interface IResultFn {

	void execute(ResultType resulType, IRequest request, IResponse response) throws Throwable;

}
