package cn.vfire.web.collector3.crawler;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.crawler.defaults.DefaultCrawlerVisitor;
import cn.vfire.web.collector3.crawler.visitor.CrawlerVisitor;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.plugin.berkeley.BerkeleyDBManager;
import cn.vfire.web.collector3.tools.Tools;
import cn.vfire.web.collector3.tools.crawler.CrawlerXmlFactory;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;

public class FetcherTest {

	public static void main(String[] args) throws Exception {

		
		CrawlerConfig crawlerConfig = getCrawlerConfig();

		CrawlerVisitor visitor = getVisitor(crawlerConfig);

		DBManager dbManager = getDBManager(crawlerConfig);

		dbManager.clear();

		dbManager.open();

		dbManager.initSegmentWriter();

		dbManager.inject(new CrawlDatum("http://www.biquge.la/book/4522/", 1));

		CountDownLatch countDownLatch = new CountDownLatch(1);

		Fetcher fetcher = new Fetcher("test-fetcher", dbManager);

		fetcher.setVisitor((DefaultCrawlerVisitor) visitor);

		Fetcher.FetcherThread fthread = fetcher.new FetcherThread(countDownLatch);

		new Thread(fthread).start();

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dbManager.closeSegmentWriter();
		dbManager.close();

		System.out.println("==========================");

	}

	public static CrawlerConfig getCrawlerConfig() throws Exception {

		CrawlerXmlFactory xmlTool = CrawlerXmlFactory.getCrawlerXmlTool();
		String filePath = ClassLoader.getSystemResource("crawler-config.xml").getFile();

		File xml = new File(filePath);

		xmlTool.parseXmlFile(xml);

		return xmlTool.getCrawler("crawlerJob");

	}

	public static CrawlerVisitor getVisitor(CrawlerConfig config) {
		DefaultCrawlerVisitor visitor = new DefaultCrawlerVisitor();
		Tools.setWareObj(visitor, config, null);
		return visitor;
	}

	public static DBManager getDBManager(CrawlerConfig config) {
		BerkeleyDBManager dbManager = new BerkeleyDBManager();
		Tools.setWareObj(dbManager, config, null);
		return dbManager;
	}
}
