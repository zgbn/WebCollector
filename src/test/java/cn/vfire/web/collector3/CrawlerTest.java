package cn.vfire.web.collector3;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import cn.vfire.web.collector3.crawler.Crawler;
import cn.vfire.web.collector3.crawler.pool.DefaultFetcherPool;
import cn.vfire.web.collector3.crawler.pool.DefaultTaskPool;
import cn.vfire.web.collector3.tools.crawler.CrawlerXmlFactory;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;

public class CrawlerTest {

	public static void main(String[] args) {

		CrawlerTest test = new CrawlerTest();

		CrawlerConfig config = test.getConfig("crawlerJob");

		Crawler crawler = new Crawler(config);

		crawler.setFetcherPool(new DefaultFetcherPool());
		crawler.setTaskPool(new DefaultTaskPool());

		crawler.start();

		System.out.println("========结束:" + crawler.getRuntime() + "ms 共完成" + crawler.getTotalCount() + "次任务。=======");

	}


	private CrawlerConfig getConfig(String name) {

		try {
			CrawlerXmlFactory xmlTool = CrawlerXmlFactory.getCrawlerXmlTool();

			String filePath = ClassLoader
					.getSystemResource("cn/vfire/web/collector3/tools/crawler/xml/crawler-config.xml").getFile();

			File xml = new File(filePath);

			xmlTool.parseXmlFile(xml);

			CrawlerConfig config = xmlTool.getCrawler(name);

			return config;
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
