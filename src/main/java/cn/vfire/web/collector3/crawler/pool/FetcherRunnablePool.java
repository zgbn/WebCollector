package cn.vfire.web.collector3.crawler.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.crawldb.Generator;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;

@Slf4j
public final class FetcherRunnablePool extends FetcherThreadPool {

	private int queueSize = 10000;

	private int waitTime = 1000;

	private int waitMax = 60;

	private boolean stop;

	private void end(DBManager dbManager, Generator generator) throws CrawlerDBException {

		this.stop = true;

		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		generator.close();
		dbManager.closeSegmentWriter();
		dbManager.close();
	}

	@Override
	public int getActiveThreads() {
		return this.fetcher.getActivThreads();
	}

	private void init(DBManager dbManager, Generator generator) throws CrawlerDBException {
		this.stop = false;
		dbManager.open();
		dbManager.initSegmentWriter();
		generator.open();
	}

	private void monitorStop(final ThreadPoolExecutor threadPool) {

		int activeThreads = 0;

		while (true) {

			activeThreads = this.fetcher.getActivThreads();

			log.info("爬虫{}运行中当前活动线程数{}个。", super.name, activeThreads);

			if (threadPool.getActiveCount() == 0 && stop) {
				break;
			}

			try {
				Thread.sleep(this.waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void run() {

		final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(super.initThread, super.maxThread, super.keepAliveTime, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(this.queueSize), new ThreadPoolExecutor.DiscardOldestPolicy());

		final DBManager dbManager = this.fetcher.getDbManager();

		final Generator generator = dbManager.getGenerator();

		try {

			this.init(dbManager, generator);

			this.runFetcher(threadPool, generator);

			this.monitorStop(threadPool);

			this.end(dbManager, generator);

		} catch (CrawlerDBException e) {

			log.error("爬虫{}访问数据库操作发生异常。", super.name, e);

		}

	}

	private void runFetcher(final ThreadPoolExecutor threadPool, final Generator generator) {

		new Thread(new Runnable() {
			@Override
			public void run() {

				int count = 0;

				while (true) {

					CrawlDatum crawlDatum = null;

					if ((count++) >= waitMax) {
						stop = true;
						break;
					}

					if ((crawlDatum = generator.next()) == null) {
						try {
							Thread.sleep(waitTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}

					threadPool.execute(fetcher.new FetcherRunnable(crawlDatum));

					count = 0;

				}

				threadPool.shutdown();

			}
		}).start();
	}
}
