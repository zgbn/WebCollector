package cn.vfire.web.collector3.crawler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector3.crawler.event.DefaultCrawlerEvent;
import cn.vfire.web.collector3.crawler.executor.DefaultExecutor;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.tools.crawler.snapshot.CrawlSnapshot;
import cn.vfire.web.collector3.tools.executor.Requester;
import cn.vfire.web.collector3.tools.pool.TaskPool;

/**
 * 页面访问数据抓取
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class Fetcher {

	public class FetcherThread implements Runnable {

		private CountDownLatch countDownLatch;

		private int serialNumber;

		private String id;


		public FetcherThread(int serialNumber) {
			this.serialNumber = serialNumber;
			this.id = String.format("%s-%d", name, serialNumber);
		}


		public FetcherThread(int serialNumber, CountDownLatch countDownLatch) {
			this.serialNumber = serialNumber;
			this.id = String.format("%s-%d", name, serialNumber);
			this.countDownLatch = countDownLatch;
		}


		private void doWork() {
			// 事件触发
			Fetcher.this.event.facherStart(this.serialNumber, Fetcher.this.taskPool);

			// 事件计数器
			int exceptionCount = 0, exeCount = 0;

			boolean isException = false;

			String exceptionMsg = null;

			// 执行任务
			if (Fetcher.this.executor != null) {

				CrawlDatum crawlDatum = null;

				Page page = null;

				while ((crawlDatum = Fetcher.this.taskPool.poll()) != null) {

					// 执行任务
					{
						isException = false;

						try {

							crawlDatum.incExeCountAndGet();

							// 事件触发
							Fetcher.this.event.facherBefore(crawlDatum, Fetcher.this.taskPool);

							if (crawlDatum.isInvalid() == false) {
								// 执行抓取动作
								page = Fetcher.this.executor.execute(crawlDatum);

								Fetcher.this.visitor.fetchResultData(page);

								Fetcher.this.visitor.fetchCrawlDatum(page, Fetcher.this.taskPool);
							}

							// 事件触发
							Fetcher.this.event.facherAfer(page, Fetcher.this.taskPool);

						}
						catch (Exception e) {

							isException = true;

							exceptionMsg = String.format("%s:%s", e.getClass().getName(), e.getMessage());

							exceptionCount = exceptionCount + 1;

							// 异常快照
							Fetcher.this.exceptionNapshot();

							// 事件触发
							Fetcher.this.event.facherExceptin(crawlDatum, Fetcher.this.taskPool, e);

						}
						finally {

							exeCount = exeCount + 1;

							Fetcher.this.crawlDatumCount.incrementAndGet();

							// 运行快照
							Fetcher.this.runNapshot();

							if (isException) {
								log.warn("该爬虫触手{}完成第{}次抓取任务时发生异常，任务URL={} ExceptionMessage={}", this.id, exceptionCount,
										crawlDatum.getUrl(), exceptionMsg);
							}
							else {
								log.debug("该爬虫触手{}完成第{}次抓取任务，耗时{}毫秒，任务URL={}", this.id, exeCount,
										page.getResponseTime(), crawlDatum.getUrl());
							}
						}
					}
				}

			}
			else {
				log.warn("该爬虫触手{}没有执行对象executor={}。", this.id, Fetcher.this.executor);
			}

			// 事件触发
			Fetcher.this.event.facherEnd(this.serialNumber, exeCount, Fetcher.this.taskPool);

		}


		@Override
		public final void run() {

			Fetcher.this.activeThreads.incrementAndGet();

			Thread.currentThread().setName(this.id);

			this.doWork();

			if (this.countDownLatch != null) {
				this.countDownLatch.countDown();
			}

		}

	}


	@Setter
	private DefaultExecutor executor;

	@Setter
	private CrawlerVisitor visitor;

	@Setter
	private CrawlSnapshot snapshot;

	@Setter
	private DefaultCrawlerEvent event;

	@Setter
	private TaskPool taskPool;

	@Getter
	private String name;

	/** 活动的线程 */
	private AtomicInteger activeThreads = new AtomicInteger(0);

	/**
	 * 总共完成多少任务
	 */
	private AtomicInteger crawlDatumCount = new AtomicInteger(0);


	/**
	 * 爬虫触手构造器
	 * 
	 * @param name
	 *            爬虫名字
	 */
	public Fetcher(String name) {
		this.name = name;
	}


	/**
	 * 爬虫触手构造器
	 * 
	 * @param name
	 *            爬虫名字
	 * @param taskPool
	 *            任务池
	 */
	public Fetcher(String name, TaskPool taskPool) {
		this.name = name;
		this.setTaskPool(taskPool);
		this.executor.setRequester(this.executor);
	}


	private void exceptionNapshot() {
		if (this.snapshot != null) {
			this.snapshot.incrementAndGetShapshotExceptionCount();
		}
	}


	public int getCrawlDatumCount() {
		return crawlDatumCount.get();
	}


	public int getActiveThreads() {
		return this.activeThreads.get();
	}


	private void runNapshot() {
		if (this.snapshot != null) {
			this.snapshot.incrementAndGetShapshotRunCount();
			this.snapshot.incrementShapshotRuntime();
		}
	}


	public void setRequester(Requester requester) {
		this.executor.setRequester(requester);
	}

}
