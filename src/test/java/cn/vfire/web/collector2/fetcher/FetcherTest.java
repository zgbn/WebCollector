package cn.vfire.web.collector2.fetcher;

import cn.vfire.web.collector2.crawler.fetcher.Fetcher;
import cn.vfire.web.collector2.event.CrawlerEvent;
import cn.vfire.web.collector2.model.CrawlDatum;
import cn.vfire.web.collector2.model.Page;
import cn.vfire.web.collector2.tools.DefaultExecutor;
import cn.vfire.web.collector2.tools.Executor;
import cn.vfire.web.collector2.tools.TaskPool;

public class FetcherTest {

	private String name = "crawlerJob";


	public static void main(String[] args) {

		FetcherTest test = new FetcherTest();

		Executor executor = test.getExecutor();

		CrawlerEvent event = test.getCrawlerEvent();

		TaskPool taskPool = test.getTaskPool();

		Fetcher fetcher = new Fetcher(test.name, executor, taskPool);

		fetcher.addEvent(event);

		new Thread(fetcher.new FetcherThread(1)).start();
		new Thread(fetcher.new FetcherThread(2)).start();
		new Thread(fetcher.new FetcherThread(3)).start();
		new Thread(fetcher.new FetcherThread(4)).start();
	}


	private Executor getExecutor() {
		Executor executor = new DefaultExecutor();
		return executor;
	}


	private CrawlerEvent getCrawlerEvent() {
		CrawlerEvent event = new CrawlerEvent() {

			@Override
			public int index() {
				// TODO Auto-generated method stub
				return 0;
			}


			@Override
			public void crawlerBefore() {
				// TODO Auto-generated method stub

			}


			@Override
			public void crawlerAfer() {
				// TODO Auto-generated method stub

			}


			@Override
			public void crawlerAferAsyn() {
				// TODO Auto-generated method stub

			}


			@Override
			public boolean facherBefore(CrawlDatum crawlDatum, TaskPool taskPool, int count) {
				return true;
			}


			@Override
			public void facherAferAsyn(Page page, TaskPool taskPool, int count) {
			}


			@Override
			public boolean facherAfer(Page page, TaskPool taskPool, int count) {
				return true;
			}


			@Override
			public boolean facherExceptin(CrawlDatum crawlDatum, TaskPool taskPool, Exception e, int count) {
				return true;
			}


			@Override
			public boolean facherStart(String name, int serialNumber, TaskPool taskPool) {
				return true;
			}


			@Override
			public void facherEnd(String name, int serialNumber, int exceptionCount, TaskPool taskPool) {
			}
		};
		return event;
	}


	private TaskPool getTaskPool() {

		TaskPool taskPool = new TaskPool(this.name) {
		};

		String[] urls = new String[] { "http://www.biquge.la/book/14/9609.html",
				"http://www.biquge.la/book/14/9610.html", "http://www.biquge.la/book/14/9611.html",
				"http://www.biquge.la/book/14/9612.html", "http://www.biquge.la/book/14/9613.html",
				"http://www.biquge.la/book/14/9614.html", "http://www.biquge.la/book/14/9615.html",
				"http://www.biquge.la/book/14/9616.html", "http://www.biquge.la/book/14/9618.html",
				"http://www.biquge.la/book/14/9619.html", "http://www.biquge.la/book/14/9620.html",
				"http://www.biquge.la/book/14/9621.html", "http://www.biquge.la/book/14/9622.html",
				"http://www.biquge.la/book/14/9623.html", "http://www.biquge.la/book/14/9626.html",
				"http://www.biquge.la/book/14/9625.html", "http://www.biquge.la/book/14/9624.html",
				"http://www.biquge.la/book/14/9627.html", "http://www.biquge.la/book/14/9628.html",
				"http://www.biquge.la/book/14/9629.html", "http://www.biquge.la/book/14/9632.html" };

		for (String url : urls) {
			taskPool.add(new CrawlDatum(url));
		}

		return taskPool;

	}

}
