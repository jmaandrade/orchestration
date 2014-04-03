package pt.jma.orchestration.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URI;

import org.junit.Test;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IRequest;
import pt.jma.orchestration.activity.IResponse;
import pt.jma.orchestration.activity.Request;
import pt.jma.orchestration.context.IActivityContext;
import pt.jma.orchestration.context.XMLActivityContext;
import pt.jma.orchestration.util.thread.ThreadActivity;

public class TestHelloWorld {

	@Test
	public void test_1() {
		try {
			IActivityContext context = new XMLActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("teste");
			IRequest request = new Request();
			request.put("name", "jma1");
			IResponse response = activity.invoke(request);

			assertNotNull(response.get("sayHi"));
			assertNotNull(response.get("age"));

			System.out.printf("test_1() global-state.last-name=%s age=%s\n\n", (String) context.getState().get("last-name"),
					response.get("age"));

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void test_2() {
		try {
			IActivityContext context = new XMLActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("teste");
			IRequest request = new Request();
			request.put("name", "jma2");
			ThreadActivity thread = activity.invokeAsynchr(request);
			thread.wait4Response(5000);
			assertNotNull(thread.getResponse().get("sayHi"));
			System.out.printf("test_2() %s global-state.last-name=%s\n\n", thread.getResponse().get("sayHi"), (String) context.getState()
					.get("last-name"));
		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void test_4() {
		try {
			IActivityContext context = new XMLActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("teste2");
			IRequest request = new Request();
			request.getContext().put("language", "pt_PT");
			request.put("name", "jma1");
			IResponse response = activity.invoke(request);
			assertNotNull(response.get("sayHi"));
			assertNotNull(response.get("estado"));
			System.out.printf("\ntest_4() %s estado=%s\n\n", (String) response.get("sayHi"), (String) response.get("estado"));
		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void test_5() {
		try {
			IActivityContext context = new XMLActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("teste3");
			IRequest request = new Request();
			request.getContext().put("language", "pt_PT");
			request.put("name", "jma5");
			IResponse response = activity.invoke(request);
			assertNotNull(response.get("sayHi"));
			assertNotNull(response.get("estado"));
			assertNotNull(response.get("estado2"));
			System.out.printf("\ntest_5() %s estado=%s\n\n", (String) response.get("sayHi"), (String) response.get("estado"));
		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void test_6() {
		try {
			IActivityContext context = new XMLActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("teste4");
			IRequest request = new Request();
			IResponse response = activity.invoke(request);
			assertNotNull(response.get("estado"));
			System.out.printf("\ntest_6() estado=%s\n\n", (String) response.get("estado"));
		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

}
