package pt.jma.orchestration.test;

import pt.jma.common.atomic.AbstractMapUtilEntryFn;
import pt.jma.common.atomic.IAtomicMapUtil;

public class Worker2 extends Thread {

	Integer ID;
	IAtomicMapUtil am;
	AbstractMapUtilEntryFn<String> fn;
	String prefix;

	public Worker2(Integer iD, IAtomicMapUtil am, AbstractMapUtilEntryFn<String> fn, String prefix) {
		super();
		ID = iD;
		this.am = am;
		this.fn = fn;
		this.prefix = prefix;
	}

	public void run() {
		try {
			String result = am.swapEntry(fn).toString();
			System.out.printf("\n%s%d, %s", this.prefix, this.ID, result);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		synchronized (am) {
			if (++TestIMapUtilPessimisticLocking.finished == TestIMapUtilPessimisticLocking.total)
				am.notifyAll();
		}

	}
}
