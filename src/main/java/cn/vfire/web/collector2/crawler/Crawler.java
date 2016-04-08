package cn.vfire.web.collector2.crawler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.vfire.web.collector.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector2.crawler.fetcher.Fetcher;
import cn.vfire.web.collector2.crawler.fetcher.DefaultFetcherPool;
import cn.vfire.web.collector2.event.CrawlerEvent;
import cn.vfire.web.collector2.tools.Executor;
import cn.vfire.web.collector2.tools.FetcherThreadPool;
import cn.vfire.web.collector2.tools.TaskPool;
import lombok.Setter;

/**
 * 爬虫，一个爬虫只执行一个爬虫批任务。
 * 
 * @author ChenGang
 *
 */
public class Crawler {

	private CrawlerConfig config;

	private Fetcher fetcher;

	@Setter
	private FetcherThreadPool fetcherPool;


	public Crawler(CrawlerConfig config) {
		this.config = config;
		this.fetcher = new Fetcher(config.getId());
		this.fetcherPool = new DefaultFetcherPool(this.fetcher);
	}


	public Crawler(CrawlerConfig config, Executor executor) {
		this.config = config;
		this.fetcher = new Fetcher(config.getId());
		this.fetcher.setExecutor(executor);
		this.fetcherPool = new DefaultFetcherPool(this.fetcher);
	}


	public Crawler(CrawlerConfig config, Executor executor, TaskPool taskPool) {
		this.config = config;
		this.fetcher = new Fetcher(config.getId());
		this.fetcher.setExecutor(executor);
		this.fetcher.setTaskPool(taskPool);
		this.fetcherPool = new DefaultFetcherPool(this.fetcher);
	}


	/**
	 * 添加爬虫事件
	 * 
	 * @param event
	 */
	public void addEvent(CrawlerEvent event) {
		this.fetcher.addEvent(event);
	}


	/**
	 * 注入执行者
	 * 
	 * @param executor
	 */
	public void setExecutor(Executor executor) {
		this.fetcher.setExecutor(executor);
	}


	public void setSnapshot(CrawlSnapshot snapshot) {
		if (this.fetcher != null && snapshot != null) {
			this.fetcher.setSnapshot(snapshot);
		}
	}


	/**
	 * 注入任务池，任务池中有数据爬虫才会工作。
	 * 
	 * @param taskPool
	 */
	public void setTaskPool(TaskPool taskPool) {
		this.fetcher.setTaskPool(taskPool);
	}


	public void start() {
		this.startFetcherPool();
	}


	public long getRuntime() {
		return this.fetcherPool.getRuntime();
	}


	private void startFetcherPool() {

		this.fetcherPool.setInc(this.config.getIncthreads());
		this.fetcherPool.setInitThread(this.config.getThreads());
		this.fetcherPool.setMaxThread(this.config.getMaxthreads());
		this.fetcherPool.setMinThread(this.config.getMinthreads());

		this.fetcherPool.execute();

	}


	@SuppressWarnings("unused")
	private void startCountDownLatch(int threads) {

		this.fetcher.setThreads(threads);

		ExecutorService exeFatcher = Executors.newFixedThreadPool(this.fetcher.getThreads());

		CountDownLatch countLatch = new CountDownLatch(threads);

		for (int i = 0; i < threads; i++) {

			Fetcher.FetcherThread ftd = this.fetcher.new FetcherThread(i, countLatch);

			exeFatcher.execute(ftd);
		}

		exeFatcher.shutdown();

		try {
			countLatch.await();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
