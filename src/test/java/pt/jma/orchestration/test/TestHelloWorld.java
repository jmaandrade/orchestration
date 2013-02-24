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

public class TestHelloWorld {

	@Test
	public void test() {
		try {
			IActivityContext context = new ActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("teste");
			IRequest request = new Request();
			request.put("name", "jma");
			IResponse response = activity.invoke(request);

			assertNotNull(response.get("sayHi"));

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

}
