package pt.jma.orchestration.test;

import pt.jma.common.atomic.IAtomic;
import pt.jma.common.atomic.IFn;

public class Worker extends Thread {

	Integer ID;
	IAtomic<Integer> count;
	IFn fn;
	int total;
	String prefix;

	public Worker(Integer iD, IAtomic<Integer> count, IFn fn, int total, String prefix) {
		super();
		ID = iD;
		this.count = count;
		this.fn = fn;
		this.total = total;
		this.prefix = prefix;
	}

	public void run() {
		Integer result = count.swap(fn);
		System.out.printf("\n%s%d, %d", this.prefix, this.ID, result);

		synchronized (count) {
			if (result.intValue() == total)
				count.notifyAll();
		}

	}
}
