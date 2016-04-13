package cn.vfire.web.collector3.crawler.pool;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.crawler.Fetcher.FetcherThread;
import cn.vfire.web.collector3.tools.pool.FetcherThreadPool;

public final class DefaultFetcherPool extends FetcherThreadPool implements Default {

	private LinkedList<FetcherThread> queue = new LinkedList<FetcherThread>();

	private ExecutorService exeFatcher = Executors.newCachedThreadPool();

	@Getter
	private int threadCount = 0;

	@Override
	public void decrementThread() {
	}

	@Override
	public void incrementThead() {
	}

	@Override
	protected void run() {

		this.exeFatcher = Executors.newFixedThreadPool(super.initThread);

		CountDownLatch countDownLatch = new CountDownLatch(super.initThread);

		for (int i = 0; i < super.initThread; i++) {

			FetcherThread thread = this.fetcher.new FetcherThread(i, countDownLatch);

			this.queue.add(thread);

			this.exeFatcher.execute(thread);

			this.threadCount++;

		}

		exeFatcher.shutdown();

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
