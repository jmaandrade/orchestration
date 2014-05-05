package pt.jma.orchestration.test;

import static org.junit.Assert.fail;

import java.net.URI;

import org.junit.Test;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.Request;
import pt.jma.orchestration.context.IActivityContext;
import pt.jma.orchestration.context.XMLActivityContext;

public class TestHelloWord {

	@Test
	public void test_1() {
		try {
			IActivityContext context = new XMLActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("teste");
			IRequest request = new Request();
			request.put("name", "jma1");
			IResponse response = activity.invoke(request);

			System.out.printf("test_1() %s global-state.last-name=%s age=%s\n\n", response.get("sayHi"),
					(String) context.getState().get("last-name"), response.get("age"));

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

}
