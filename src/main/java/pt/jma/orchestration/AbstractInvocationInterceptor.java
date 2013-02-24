package pt.jma.orchestration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInvocationInterceptor implements InvocationHandler {

	Object targetInstance;

	public Object getTargetInstance() throws Exception {
		return targetInstance;
	}

	public void setTargetInstance(Object targetInstance) throws Exception {
		this.targetInstance = targetInstance;
	}

	Map<String, String> propertiesMap = new HashMap<String, String>();

	public Map<String, String> getPropertiesMap() {
		return propertiesMap;
	}

	abstract public void initTarget() throws Exception;

	final public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		try {

			if (method.getName().equals("invoke")) {

				initTarget();

				beforeInvoke((IRequest) args[0]);

				IResponse response = (IResponse) method.invoke(this.targetInstance, args);

				afterInvoke(response);

				return response;

			} else {
				return method.invoke(this.targetInstance, args);
			}
		} catch (InvocationTargetException ex) {
			Exception cause = (Exception) ex.getCause();
			this.handleException(cause);

		} catch (Exception ex) {
			this.handleException(ex);
		}
		return null;

	}

	abstract public void beforeInvoke(IRequest request) throws Exception;

	abstract public void afterInvoke(IResponse response) throws Exception;

	abstract public void handleException(Exception ex) throws Exception;

}
