package pt.jma.orchestration.state;

public interface IMapUtilEntryFn<T> {

	T call() throws Throwable;

	String getKey();

}
