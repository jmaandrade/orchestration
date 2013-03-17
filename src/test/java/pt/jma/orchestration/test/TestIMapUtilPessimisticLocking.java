package pt.jma.orchestration.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Comparator;

import org.junit.Test;

import pt.jma.common.IMapUtil;
import pt.jma.common.MapUtil;
import pt.jma.orchestration.state.IAtomicMapUtil;
import pt.jma.orchestration.state.PessimisticMapUtilLocking;

public class TestIMapUtilPessimisticLocking {

	IMapUtil map = new MapUtil();

	static IAtomicMapUtil am;
	static int total = 50;
	static int finished = 0;

	@Test
	public void test_1() {
		try {
			map.put("TESTE", ":(");
			am = new PessimisticMapUtilLocking(map);

			am.compareAndSet("TESTE", new Comparator<String>() {
				public int compare(String o1, String o2) {
					return o1.equals(o2) ? 0 : -1;
				}
			}, ":(", ";)");

			for (int i = 0; i < total; i++) {
				new Worker2(i, am, new ConcatWithUpperCaseFn(am.getValue(), "TESTE", "jmaa-".concat(new Integer(i).toString())), "--")
						.start();

			}

			synchronized (am) {
				am.wait(5000);
			}

			assertNotNull(am.getValue().get("TESTE"));
			System.out.printf("teste = %s", am.getValue().get("TESTE"));
			System.out.println("TestIMapUtilPessimisticLocking Version 1.0.7.0");

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
}
