package pt.jma.orchestration.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.concurrent.Executors;

import org.junit.Test;

import pt.jma.common.collection.CollectionUtil;
import pt.jma.common.collection.IMapProcessor;
import pt.jma.orchestration.ActivityContext;
import pt.jma.orchestration.IActivity;
import pt.jma.orchestration.IActivityContext;
import pt.jma.orchestration.IRequest;
import pt.jma.orchestration.IResponse;
import pt.jma.orchestration.Request;
import pt.jma.orchestration.util.thread.ThreadActivity;
import pt.jma.orchestration.util.thread.ThreadPoolActivity;

public class TestHelloWorld {

	@Test
	public void test_1() {
		try {
			IActivityContext context = new ActivityContext(new URI("src/test/resources/context.xml"));
			IActivity activity = context.lookup("teste");
			IRequest request = new Request();
			request.put("name", "jma1");
			IResponse response = activity.invoke(request);

			assertNotNull(response.get("sayHi"));

			System.out.printf("test_1() global-state.last-name=%s\n\n", (String) context.getState().get("last-name"));

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void test_2() {
		try {
			IActivityContext context = new ActivityContext(new URI("src/test/resources/context.xml"));
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
	public void test_3() {
		try {
			IActivityContext context = new ActivityContext(new URI("src/test/resources/context.xml"));
			ThreadPoolActivity pool = new ThreadPoolActivity(Executors.newFixedThreadPool(5));

			for (int i = 0; i < 2; i++) {
				IRequest request = new Request();
				request.put("name", String.format("jmandrade-%d", i));
				IActivity activity = context.lookup("teste");
				pool.add(activity, request);
			}
			pool.wait4AllResponses(5000);

			CollectionUtil.map(pool.getList(), new IMapProcessor<ThreadActivity>() {
				public boolean execute(ThreadActivity t) throws Throwable {
					if (t.getResponse() != null)
						System.out.printf("test_3() t.getResponse().get(sayHi)=%s\n", t.getResponse().get("sayHi"));

					return true;

				}
			});
			assertNotNull(context.getState().get("last-name"));
			System.out.printf("test_3() global-state.last-name=%s", (String) context.getState().get("last-name"));

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void test_4() {
		try {
			IActivityContext context = new ActivityContext(new URI("src/test/resources/context.xml"));

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

}
