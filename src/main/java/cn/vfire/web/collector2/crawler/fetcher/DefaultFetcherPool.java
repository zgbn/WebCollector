package cn.vfire.web.collector2.crawler.fetcher;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.vfire.web.collector2.crawler.fetcher.Fetcher.FetcherThread;
import cn.vfire.web.collector2.tools.FetcherThreadPool;
import lombok.Getter;

public final class DefaultFetcherPool extends FetcherThreadPool {

	private LinkedList<FetcherThread> queue = new LinkedList<FetcherThread>();

	private ExecutorService exeFatcher = Executors.newCachedThreadPool();

	@Getter
	private int threadCount = 0;


	public DefaultFetcherPool(Fetcher fetcher) {
		super(fetcher);
	}


	@Override
	public void decrementThread() {
		int count = 0;
		for (FetcherThread ft : queue) {
			if (count < this.inc) {
				if (ft.isRun()) {
					ft.setRun(false);
					this.threadCount--;
				}
			}
		}
	}


	@Override
	public void incrementThead() {

		int count = 0;
		for (FetcherThread ft : queue) {
			if (count < this.inc) {
				if (ft.isRun() == false) {
					ft.setRun(true);
					this.threadCount++;
				}
			}
		}

	}


	@Override
	protected void run() {

		CountDownLatch countDownLatch = new CountDownLatch(super.maxThread);

		int count = 0;

		for (int i = 0; i < super.maxThread; i++) {

			FetcherThread thread = this.fetcher.new FetcherThread(i, countDownLatch);

			this.queue.add(thread);

			if (count >= this.initThread) {
				thread.setRun(false);
			}

			if (thread.isRun()) {
				this.exeFatcher.execute(thread);
				this.threadCount++;
			}
		}

		try {
			countDownLatch.await();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
