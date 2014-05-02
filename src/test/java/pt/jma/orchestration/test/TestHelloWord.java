package pt.jma.orchestration.test;

import org.junit.Test;

public class TestDummy {

	public int fibonacci(int n) {
		return (n < 2) ? n : fibonacci(n - 1) + fibonacci(n - 2);
	}

	@Test
	public void test_1() {

		int[] s = new int[10];
		for (int i = 0; i < 10; i++) {
			s[i] = fibonacci(i);
		}

		for (int n : s) {
			if ((n % 2) == 0) {
				System.out.printf("%d", n);
			}
		}
	}

}
