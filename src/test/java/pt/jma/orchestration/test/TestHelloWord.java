package pt.jma.orchestration.test;

import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URI;

import org.junit.Test;

import pt.jma.orchestration.activity.IActivity;
import pt.jma.orchestration.activity.IEventActivityObserver;
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

			activity.addEventObserver(new IEventActivityObserver() {

				public void update(String event) {
					System.out.printf(String.format("Event %s fired!!\n", event));
				}

			});

			IRequest request = new Request();
			request.put("name", "jma1");
			IResponse response = activity.invoke(request);

			System.out.printf("test_1() %s global-state.last-name=%s age=%s\n\n", response.get("sayHi"),
					(String) context.getState().get("last-name"), response.get("age"));

			ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("teste.ser")));
			output.writeObject(activity);
			output.close();

			activity = null;

			ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("teste.ser")));
			activity = (IActivity) input.readObject();
			input.close();

			System.out.printf("test_1(), (from serialized activity) activity-state.age=%s \n\n", (String) activity.getState().get("age"));

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

}
