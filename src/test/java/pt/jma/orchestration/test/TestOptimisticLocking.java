package pt.jma.orchestration.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import pt.jma.orchestration.state.IAtomic;
import pt.jma.orchestration.state.OptimisticLocking;

public class TestOptimisticLocking {

	static int total = 2;
	Map<String, IAtomic<Integer>> map = new HashMap<String, IAtomic<Integer>>();

	@Test
	public void test_1() {
		try {
			map.put("teste", new OptimisticLocking<Integer>(0));
			IAtomic<Integer> count = map.get("teste");

			for (int i = 0; i < total; i++) {
				new Worker(i, count, new DummyFn(new Integer(i)), total, "--").start();

			}

			synchronized (count) {
				count.wait(5000);
			}

			count.compareAndSet(new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					if (o1.intValue() == o2.intValue())
						return 0;
					return -1;
				}

			}, new Integer(total + 1), new Integer(1968));

			assertNotNull(count);
			System.out.printf("\n<<%d", count.getValue());

			System.out.println("OptimisticLocking Version 1.0.7.0");

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
}
