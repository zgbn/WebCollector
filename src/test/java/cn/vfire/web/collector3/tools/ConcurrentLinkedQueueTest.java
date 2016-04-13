package cn.vfire.web.collector3.tools;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueTest {

	private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();

	public static void main(String[] args) {

		System.out.println(queue);

		Runnable run1 = new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while (true) {
					queue.offer(i);
					System.out.println("T1生产:" + i);
					i++;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Runnable run2 = new Runnable() {
			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Integer r = queue.poll();
					if (r != null) {
						System.out.println("T2消耗:" + r);
					}

				}
			}
		};

		Runnable run3 = new Runnable() {
			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Integer r = queue.poll();
					if (r != null) {
						System.out.println("T3消耗:" + r);
					}

				}
			}
		};

		Thread t1 = new Thread(run1);
		Thread t2 = new Thread(run2);
		Thread t3 = new Thread(run3);

		t1.start();
		t2.start();
		t3.start();

	}
}
