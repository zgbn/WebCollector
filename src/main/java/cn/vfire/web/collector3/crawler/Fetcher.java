package cn.vfire.web.collector3.crawler;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.crawldb.Generator;
import cn.vfire.web.collector3.crawler.defaults.DefaultCrawlerVisitor;
import cn.vfire.web.collector3.crawler.defaults.DefaultExecutor;
import cn.vfire.web.collector3.crawler.executor.Executor;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.lang.CrawlerException;
import cn.vfire.web.collector3.lang.FatchStopException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Links;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.ResultData;

/**
 * 页面访问数据抓取
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class Fetcher {

	/**
	 * 触手任务。
	 * 
	 * @author ChenGang
	 *
	 */
	public class FetcherRunnable implements Runnable {

		private CountDownLatch countDownLatch;

		private CrawlDatum crawlDatum;

		public FetcherRunnable(CrawlDatum crawlDatum) {
			this.crawlDatum = crawlDatum;
		}

		public FetcherRunnable(CrawlDatum crawlDatum, CountDownLatch countDownLatch) {
			this.crawlDatum = crawlDatum;
			this.countDownLatch = countDownLatch;
		}

		private void doWork() throws Exception {

			Page page = null;

			if (Fetcher.this.visitor.fetchCrawlDatum(crawlDatum)) {// 是否跳过该任务

				try {

					page = Fetcher.this.executor.execute(crawlDatum);// 执行抓取动作

					crawlDatum.setStatus(CrawlDatum.STATUS_DB_SUCCESS);// 抓取成功后设置该任务状态

					Fetcher.this.totalCount.incrementAndGet();// 计数器记录抓取成功的任务记录

					Links links = Fetcher.this.visitor.fetchParseLinks(page, null);// 分析是否有新的提取任务

					Fetcher.this.visitor.fetchResultData(page, new LinkedList<ResultData>());

					log.debug("爬虫保存提取到的新的Link任务。");

					Fetcher.this.dbManager.wrtieParseSegment(links);// 保存新的提取任务

				} catch (CrawlerException e) {

					crawlDatum.setStatus(CrawlDatum.STATUS_DB_FAILED);// 抓取数据失败发生异常。

					crawlDatum.incExpCountAndGet();// 异常计数器+1

					log.debug("爬虫保存提取发生异常的任务。", e);

				} finally {

					this.crawlDatum.incExeCountAndGet();// 当前任务总执行次数+1

					if (this.crawlDatum.getStatus() == CrawlDatum.STATUS_DB_SUCCESS) {
						log.debug("爬虫删除爬取成功的任务。");
						Fetcher.this.dbManager.deleteFetchSegment(this.crawlDatum);// 任务成功则从任务池中删除。
					} else {
						log.debug("爬虫保存爬取失败的任务。");
						Fetcher.this.dbManager.wrtieFetchSegment(this.crawlDatum);// 否则将该任务重新放回任务池中。
					}

				}

			}

		}

		@Override
		public final void run() {

			Fetcher.this.activThreads.incrementAndGet();

			if (Fetcher.this.stop.get()) {
				return;
			}

			try {
				this.doWork();
			} catch (FatchStopException e) {
				log.warn("爬虫{}触手运行中不满足条件停止。", Fetcher.this.name, e);
				Fetcher.this.stop.set(true);
			} catch (Exception e) {
				log.warn("爬虫{}触手运行发生数据库读写异常。", Fetcher.this.name, e);
			} finally {
				if (this.countDownLatch != null) {
					this.countDownLatch.countDown();
				}
			}

			Fetcher.this.activThreads.decrementAndGet();

		}
	}

	/**
	 * 触手线程级
	 * 
	 * @author ChenGang
	 *
	 */
	public class FetcherThread extends Thread {

		private CountDownLatch countDownLatch;

		public FetcherThread() {
			super(Fetcher.this.name);
		}

		public FetcherThread(CountDownLatch countDownLatch) {
			super(Fetcher.this.name);
			this.countDownLatch = countDownLatch;
		}

		private void doWork() throws CrawlerDBException, FatchStopException {

			CrawlDatum crawlDatum = null;

			Page page = null;

			Generator generator = Fetcher.this.dbManager.getGenerator();

			generator.open();

			while ((crawlDatum = generator.next()) != null) {

				try {

					if (Fetcher.this.visitor.fetchCrawlDatum(crawlDatum) == false) {
						continue;
					}

					page = Fetcher.this.executor.execute(crawlDatum);// 执行抓取动作

					Fetcher.this.totalCount.incrementAndGet();// 计数器

					Links links = Fetcher.this.visitor.fetchParseLinks(page, null);

					Fetcher.this.visitor.fetchResultData(page, new LinkedList<ResultData>());

					log.debug("爬虫保存提取到的新的Link任务。");

					Fetcher.this.dbManager.wrtieParseSegment(links);

					crawlDatum.setStatus(CrawlDatum.STATUS_DB_SUCCESS);

				} catch (Exception e) {

					crawlDatum.setStatus(CrawlDatum.STATUS_DB_FAILED);

					crawlDatum.incExpCountAndGet();

					log.debug("爬虫保存提取发生异常的任务。", e);

				} finally {

					crawlDatum.incExeCountAndGet();

					if (crawlDatum.getStatus() == CrawlDatum.STATUS_DB_SUCCESS || crawlDatum.isInvalid()) {
						log.debug("爬虫删除爬取成功的任务。");
						Fetcher.this.dbManager.deleteFetchSegment(crawlDatum);
					} else {
						log.debug("爬虫保存爬取失败的任务。");
						Fetcher.this.dbManager.wrtieFetchSegment(crawlDatum);
					}

				}

			}

			generator.close();

		}

		@Override
		public final void run() {

			Fetcher.this.activThreads.incrementAndGet();

			if (Fetcher.this.stop.get()) {
				return;
			}

			try {
				this.doWork();
			} catch (CrawlerDBException e) {
				log.warn("爬虫{}触手运行发生数据库读写异常。", Fetcher.this.name, e);
				e.printStackTrace();
			} catch (FatchStopException e) {
				log.warn("爬虫{}触手运行中不满足条件停止。", Fetcher.this.name, e);
				Fetcher.this.stop.set(true);
			} finally {
				if (this.countDownLatch != null) {
					this.countDownLatch.countDown();
				}
			}

			Fetcher.this.activThreads.decrementAndGet();

		}
	}

	@Setter
	private Executor executor = new DefaultExecutor();

	@Setter
	private DefaultCrawlerVisitor visitor = new DefaultCrawlerVisitor();

	@Setter
	@Getter
	private DBManager dbManager;

	@Getter
	private String name;

	/** 执行次数 */
	private AtomicInteger totalCount = new AtomicInteger(0);

	/** 控制运行中 */
	private AtomicBoolean stop = new AtomicBoolean(false);

	private AtomicInteger activThreads = new AtomicInteger(0);

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
	public Fetcher(String name, DBManager dbManager) {
		this.name = name;
		this.dbManager = dbManager;
	}

	/**
	 * 获取当前活动线程。
	 * 
	 * @return
	 */
	public int getActivThreads() {
		return this.activThreads.get();
	}

	public int getTotalCount() {
		return this.totalCount.get();
	}

}
