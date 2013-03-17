package pt.jma.orchestration.state;

public interface IFn<T> {

	T call() throws Throwable;

}
