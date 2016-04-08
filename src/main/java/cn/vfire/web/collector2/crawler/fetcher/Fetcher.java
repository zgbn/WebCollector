package cn.vfire.web.collector2.crawler.fetcher;

import cn.vfire.web.collector2.crawler.FetcherCountDownLatch;
import cn.vfire.web.collector2.event.CrawlerEvent;
import cn.vfire.web.collector2.event.DefaultCrawlerEvent;
import cn.vfire.web.collector2.model.CrawlDatum;
import cn.vfire.web.collector2.model.Page;
import cn.vfire.web.collector2.tools.DefaultExecutor;
import cn.vfire.web.collector2.tools.Executor;
import cn.vfire.web.collector2.tools.TaskPool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 页面访问数据抓取
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class Fetcher {

	private DefaultExecutor executor = new DefaultExecutor();

	private DefaultCrawlerEvent event = new DefaultCrawlerEvent();

	private TaskPool taskPool;

	@Getter
	private String name;


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
		if (executor != null && (executor instanceof DefaultExecutor) == false) {
			this.executor.setExecutor(executor);
		}
		this.taskPool = taskPool;
		this.name = name;

	}


	/**
	 * 添加爬虫事件
	 * 
	 * @param event
	 */
	public void addEvent(CrawlerEvent event) {
		this.event.addEvent(event);
	}


	public class FetcherThread implements Runnable {

		private FetcherCountDownLatch countDownLatch;

		private int serialNumber;

		private String id;


		public FetcherThread(int serialNumber, FetcherCountDownLatch countDownLatch) {
			this.serialNumber = serialNumber;
			this.id = String.format("%s-%d", name, serialNumber);
			this.countDownLatch = countDownLatch;
		}


		public FetcherThread(int serialNumber) {
			this.serialNumber = serialNumber;
			this.id = String.format("%s-%d", name, serialNumber);
		}


		@Override
		public void run() {

			Thread.currentThread().setName(this.id);

			this.doWork();

			if (this.countDownLatch != null) {
				this.countDownLatch.countDown();
			}

		}


		private void doWork() {

			if (Fetcher.this.event.facherStart(Fetcher.this.name, this.serialNumber, Fetcher.this.taskPool) == false) {
				return;
			}

			int exceptionCount = 0;

			if (Fetcher.this.executor != null) {

				CrawlDatum crawlDatum = null;

				while ((crawlDatum = Fetcher.this.taskPool.get()) != null) {

					try {

						if (Fetcher.this.event.facherBefore(crawlDatum, Fetcher.this.taskPool,
								exceptionCount) == false) {
							return;
						}

						final Page page = Fetcher.this.executor.execute(crawlDatum);

						page.setResponseTime(Fetcher.this.executor.getTimeConsuming());

						log.debug("该爬虫触手{}完成第{}次抓取任务，任务URL={}", this.id, exceptionCount, crawlDatum.getUrl());

						Fetcher.this.event.facherAferAsyn(page, Fetcher.this.taskPool, exceptionCount);

						if (Fetcher.this.event.facherAfer(page, Fetcher.this.taskPool, exceptionCount) == false) {
							return;
						}

						exceptionCount++;

					}
					catch (Exception e) {

						log.debug("该爬虫触手{}完成第{}次抓取任务时发生异常，任务URL={}。{}。", this.id, exceptionCount, crawlDatum.getUrl(),
								e);

						if (Fetcher.this.event.facherExceptin(crawlDatum, Fetcher.this.taskPool, e,
								exceptionCount) == false) {
							return;
						}

					}
				}

				log.info("该爬虫触手{}执行任务结束，完成{}此抓取任务。", this.id, exceptionCount);
			}
			else {
				log.info("该爬虫触手{}没有执行对象executor={}。", this.id, Fetcher.this.executor);
			}

			Fetcher.this.event.facherEnd(Fetcher.this.name, this.serialNumber, exceptionCount, Fetcher.this.taskPool);

		}

	}
}
