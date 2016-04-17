package cn.vfire.web.collector3.crawler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import cn.vfire.web.collector3.crawler.event.CrawlerEvent;
import cn.vfire.web.collector3.crawler.executor.Executor;
import cn.vfire.web.collector3.crawler.pool.Generator;
import cn.vfire.web.collector3.crawler.pool.TaskPool;
import cn.vfire.web.collector3.crawler.snapshot.CrawlSnapshot;
import cn.vfire.web.collector3.crawler.visitor.CrawlerVisitor;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 页面访问数据抓取
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class Fetcher {

	/**
	 * 触手线程级
	 * 
	 * @author ChenGang
	 *
	 */
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

				Generator generator = Fetcher.this.taskPool.getGenerator();

				while (generator.hasNext()) {

					crawlDatum = generator.next();

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
	private Executor executor;

	@Setter
	private CrawlerVisitor visitor;

	@Setter
	private CrawlSnapshot snapshot;

	@Setter
	private CrawlerEvent event;

	@Setter
	private TaskPool taskPool;

	@Getter
	private String name;

	/** 活动的线程 */
	private AtomicInteger activeThreads = new AtomicInteger(0);


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
		this.taskPool = taskPool;
	}


	public int getTotalCount() {
		return this.taskPool.getTotalGenerate();
	}


	/**
	 * 异常快照
	 */
	private void exceptionNapshot() {
		if (this.snapshot != null) {
			this.snapshot.incrementAndGetShapshotExceptionCount();
		}
	}


	/**
	 * 获取活动线程个数
	 * 
	 * @return
	 */
	public int getActiveThreads() {
		return this.activeThreads.get();
	}


	/**
	 * 运行时快照
	 */
	private void runNapshot() {
		if (this.snapshot != null) {
			this.snapshot.incrementAndGetShapshotRunCount();
			this.snapshot.incrementShapshotRuntime();
		}
	}

}
