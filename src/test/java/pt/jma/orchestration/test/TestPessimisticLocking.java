package pt.jma.orchestration.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Comparator;

import org.junit.Test;

import pt.jma.common.atomic.IAtomic;
import pt.jma.common.atomic.PessimisticLocking;

public class TestPessimisticLocking {

	static IAtomic<Integer> count;
	static int total = 2;

	@Test
	public void test_1() {
		try {

			IAtomic<Integer> count = new PessimisticLocking<Integer>(0);

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
			System.out.println("PessimisticLocking Version 1.0.7.0");

		} catch (Throwable ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
}
