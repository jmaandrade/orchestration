package pt.jma.orchestration.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URI;

import org.junit.Test;

import pt.jma.orchestration.ActivityContext;
import pt.jma.orchestration.IActivity;
import pt.jma.orchestration.IActivityContext;
import pt.jma.orchestration.IRequest;
import pt.jma.orchestration.IResponse;
import pt.jma.orchestration.Request;

public class TestClojureAction {

	@Test
	public void test_1() {

		try {
			IActivityContext context = new ActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("testeClj");
			IRequest request = new Request();
			request.put("name", "Clojure World");
			IResponse response = activity.invoke(request);

			assertNotNull(response.get("sayHi"));

			System.out.printf("test_1() sayHi=%s global-state.last-name=%s\n\n", response.get("sayHi"),
					(String) context.getState().get("last-name"));

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
}
