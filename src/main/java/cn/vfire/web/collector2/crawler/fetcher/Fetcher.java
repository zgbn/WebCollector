package cn.vfire.web.collector2.crawler.fetcher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector2.crawler.CrawlSnapshot;
import cn.vfire.web.collector2.event.CrawlerEvent;
import cn.vfire.web.collector2.event.DefaultCrawlerEvent;
import cn.vfire.web.collector2.model.CrawlDatum;
import cn.vfire.web.collector2.model.Page;
import cn.vfire.web.collector2.tools.DefaultExecutor;
import cn.vfire.web.collector2.tools.Executor;
import cn.vfire.web.collector2.tools.TaskPool;

/**
 * 页面访问数据抓取
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class Fetcher {

	@Setter
	private CrawlSnapshot snapshot;

	@Setter
	private boolean close = false;


	private void runNapshot() {
		if (this.snapshot != null) {
			this.snapshot.incrementAndGetShapshotRunCount();
			this.snapshot.incrementShapshotRuntime();
		}
	}


	private void exceptionNapshot() {
		if (this.snapshot != null) {
			this.snapshot.incrementAndGetShapshotExceptionCount();
		}
	}


	public class FetcherThread implements Runnable {

		/** true运行 ， false暂停 */
		@Setter
		@Getter
		private boolean run = true;

		private CountDownLatch countDownLatch;

		private int serialNumber;

		private String id;


		public FetcherThread(int serialNumber) {
			this.serialNumber = serialNumber;
			this.id = String.format("%s-%d", name, serialNumber);
			this.run = true;
		}


		public FetcherThread(int serialNumber, CountDownLatch countDownLatch) {
			this.serialNumber = serialNumber;
			this.id = String.format("%s-%d", name, serialNumber);
			this.countDownLatch = countDownLatch;
			this.run = true;
		}


		private void doWork() {
			// 事件触发
			if (Fetcher.this.event.facherStart(Fetcher.this.name, this.serialNumber, Fetcher.this.taskPool) == false) {
				return;
			}

			// 事件计数器
			int exceptionCount = 0, exeCount = 0;

			// 执行任务
			if (Fetcher.this.executor != null) {

				CrawlDatum crawlDatum = null;

				// 从任务队列中提取任务一直到没有任务
				while ((crawlDatum = Fetcher.this.taskPool.get()) != null) {

					// 当前线程执行开关
					if (Fetcher.this.close) {
						return;
					}

					// 当前线程状态，如果处于非运行-1，0状态，则暂停等待。
					if (this.run == false) {
						try {
							Thread.sleep(500);
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}

					try {
						// 事件触发
						if (Fetcher.this.event.facherBefore(crawlDatum, Fetcher.this.taskPool, exeCount) == false) {
							return;
						}

						// 执行抓取动作
						Page page = Fetcher.this.executor.execute(crawlDatum);

						log.debug("该爬虫触手{}完成第{}次抓取任务，耗时{}毫秒，任务URL={}", this.id, exeCount, page.getResponseTime(),
								crawlDatum.getUrl());

						// 事件触发
						Fetcher.this.event.facherAferAsyn(page, Fetcher.this.taskPool, exeCount);

						// 事件触发
						if (Fetcher.this.event.facherAfer(page, Fetcher.this.taskPool, exeCount) == false) {
							return;
						}

					}
					catch (Exception e) {

						exceptionCount = exceptionCount + 1;

						log.warn("该爬虫触手{}完成第{}次抓取任务时发生异常，任务URL={} Message={} Exception={} ", this.id, exceptionCount,
								crawlDatum.getUrl(), e.getMessage(), e.getClass().getName());

						// 异常快照
						Fetcher.this.exceptionNapshot();

						// 事件触发
						if (Fetcher.this.event.facherExceptin(crawlDatum, Fetcher.this.taskPool, e,
								exeCount) == false) {
							return;
						}

					}
					finally {

						exeCount = exeCount + 1;

						// 运行快照
						Fetcher.this.runNapshot();

					}
				}

			}
			else {
				log.warn("该爬虫触手{}没有执行对象executor={}。", this.id, Fetcher.this.executor);
			}

			// 事件触发
			Fetcher.this.event.facherEnd(Fetcher.this.name, this.serialNumber, exeCount, Fetcher.this.taskPool);

		}


		@Override
		public final void run() {

			Thread.currentThread().setName(this.id);

			this.doWork();

			this.run = false;

			if (this.countDownLatch != null) {
				this.countDownLatch.countDown();
			}

		}
	}


	private DefaultExecutor executor = new DefaultExecutor();

	private DefaultCrawlerEvent event = new DefaultCrawlerEvent();

	@Setter
	private TaskPool taskPool;

	@Getter
	private String name;

	private AtomicInteger threads = new AtomicInteger(0);


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
	 * @param serialNumber
	 *            爬虫触手的编号
	 * @param executor
	 *            执行对象
	 * @param event
	 *            事件
	 * @param taskPool
	 *            任务池
	 */
	public Fetcher(String name, Executor executor, TaskPool taskPool) {
		this.name = name;
		this.setExecutor(executor);
		this.setTaskPool(taskPool);
	}


	/**
	 * 添加爬虫事件
	 * 
	 * @param event
	 */
	public void addEvent(CrawlerEvent event) {
		this.event.addEvent(event);
	}


	/**
	 * 注入执行器对象
	 * 
	 * @param executor
	 */
	public void setExecutor(Executor executor) {
		if (executor != null && (executor instanceof DefaultExecutor) == false) {
			this.executor.setExecutor(executor);
		}
	}


	public void setThreads(int threads) {
		this.threads.set(threads);
	}


	public int getThreads() {
		return threads.get();
	}

}
