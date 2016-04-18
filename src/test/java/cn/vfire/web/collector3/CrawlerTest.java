package cn.vfire.web.collector3;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import cn.vfire.web.collector3.crawler.Crawler;
import cn.vfire.web.collector3.crawler.pool.FetcherRunnablePool;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.plugin.berkeley.BerkeleyDBManager;
import cn.vfire.web.collector3.plugin.redis.RedisDBManager;
import cn.vfire.web.collector3.tools.crawler.CrawlerXmlFactory;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;

public class CrawlerTest {

	public static void main(String[] args) throws CrawlerDBException {

		CrawlerTest test = new CrawlerTest();

		CrawlerConfig config = test.getConfig("crawlerJob");

		Crawler crawler = new Crawler(config);

		crawler.setResumable(true);

		crawler.setFetcherPool(new FetcherRunnablePool());

		crawler.setDbManager(new RedisDBManager("CrawlerDB"));
		// crawler.setDbManager(new BerkeleyDBManager());
		

		crawler.start();

	}

	private CrawlerConfig getConfig(String name) {

		try {

			CrawlerXmlFactory xmlTool = CrawlerXmlFactory.getCrawlerXmlTool();

			String filePath = ClassLoader.getSystemResource("crawler-config.xml").getFile();

			File xml = new File(filePath);

			xmlTool.parseXmlFile(xml);

			CrawlerConfig config = xmlTool.getCrawler(name);

			return config;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
