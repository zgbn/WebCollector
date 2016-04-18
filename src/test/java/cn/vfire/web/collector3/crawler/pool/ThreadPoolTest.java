package cn.vfire.web.collector3.crawler.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {

	public static class T implements Runnable {

		private static AtomicInteger count = new AtomicInteger(0);

		public static AtomicInteger activeCount = new AtomicInteger(0);

		private CountDownLatch countDownLatch;

		private CyclicBarrier cyclicBarrier;

		public T(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		public T(CyclicBarrier cyclicBarrier) {
			this.cyclicBarrier = cyclicBarrier;
		}

		public T() {

		}

		private String task;

		public T(String task) {
			this.task = task;
		}

		@Override
		public void run() {

			activeCount.incrementAndGet();

			this.countDown();

			String tname = Thread.currentThread().getName();

			System.out.println(String.format("%s--task:%s---:%d", tname, task, count.incrementAndGet()));

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			activeCount.decrementAndGet();

		}

		private void countDown() {

			if (this.countDownLatch != null) {
				this.countDownLatch.countDown();
			}

			if (this.cyclicBarrier != null) {
				try {
					this.cyclicBarrier.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void testPool_A() {

		ExecutorService executorService = Executors.newCachedThreadPool();

		System.out.println("....开始....");

		executorService.execute(new T());
		executorService.execute(new T());
		executorService.execute(new T());
		executorService.execute(new T());
		executorService.execute(new T());

		System.out.println("....shutdown....");

		executorService.shutdown();

		System.out.println("....停止....");

	}

	public void testPool_B() {

		CountDownLatch countDownLatch = new CountDownLatch(5);

		ExecutorService executorService = Executors.newCachedThreadPool();

		System.out.println("....开始....");

		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("....shutdown....");

		executorService.shutdown();

		System.out.println("....停止....");

	}

	public void testPool_B1() {

		final CountDownLatch countDownLatch = new CountDownLatch(10);

		ExecutorService executorService = Executors.newFixedThreadPool(5);

		System.out.println("....开始....");

		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));
		executorService.execute(new T(countDownLatch));

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("....shutdown....");

		executorService.shutdown();

		System.out.println("....停止....");

	}

	public void testPool_C() {

		final CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
			@Override
			public void run() {
				System.out.println("*****************************:" + Thread.currentThread().getName());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		ExecutorService executorService = Executors.newCachedThreadPool();

		System.out.println("....开始....");

		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));
		executorService.execute(new T(cyclicBarrier));

		executorService.shutdown();

		System.out.println("....结束....");

	}

	public void testPool_D() {
		// 构造一个线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
				new ThreadPoolExecutor.DiscardOldestPolicy());

		for (int i = 1; i <= 200; i++) {
			try {
				// 产生一个任务，并将其加入到线程池
				threadPool.execute(new T("task@ " + i));

				// 便于观察，等待一段时间
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		threadPool.shutdown();

		int count = 0;

		while (true) {

			System.out.println("threadPool.getActiveCount():" + threadPool.getActiveCount());

			if (T.activeCount.get() == 0) {
				if (count++ >= 10) {
					System.out.println("=================================");
					break;
				} else {
//					System.out.println("=============" + count + "====================");
				}
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count = 0;
			}

		}

	}

	public static void main(String[] args) {
		ThreadPoolTest tpool = new ThreadPoolTest();
		tpool.testPool_D();
	}

}
