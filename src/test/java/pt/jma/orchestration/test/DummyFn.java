package pt.jma.orchestration.test;

import pt.jma.orchestration.state.IFn;

public class DummyFn implements IFn<Integer> {

	Integer instance;

	public DummyFn(Integer instance) {
		super();
		this.instance = instance;
	}

	public Integer call() {

		int j = 0;

		try {
			for (long i = 0; i < 10; i++)
				if (i % 2 == 0)
					j = 1;
		} catch (Throwable t) {
			System.out.println("interrupted...");
		}

		return instance + j;
	}

}
