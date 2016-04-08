package cn.vfire.web.collector2.crawler;

import java.util.concurrent.Executors;

import cn.vfire.web.collector.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector2.crawler.fetcher.Fetcher;

/**
 * 爬虫，一个爬虫只执行一个爬虫批任务。
 * 
 * @author ChenGang
 *
 */
public class Crawler {

	private CrawlerConfig config;

	private Fetcher fetcher;


	public Crawler(CrawlerConfig config) {
		this.config = config;
	}


	public Crawler(Fetcher fetcher, CrawlerConfig config) {
		this.fetcher = fetcher;
		this.config = config;
	}


	public void start() {

		int threads = this.config.getThreads();

		java.util.concurrent.Executor exeFatcher = Executors.newCachedThreadPool();

		FetcherCountDownLatch countLatch = new FetcherCountDownLatch(threads);

		for (int i = 0; i < threads; i++) {

			Fetcher.FetcherThread ftd = this.fetcher.new FetcherThread(i, countLatch);

			exeFatcher.execute(ftd);
		}

		try {
			countLatch.await();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


	private void testCrawler() {
		// TODO 测试出最优的并发线程数。
	}

}
