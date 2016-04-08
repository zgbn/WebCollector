package cn.vfire.web.collector2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import cn.vfire.web.collector.tools.crawler.CrawlerXmlFactory;
import cn.vfire.web.collector.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector2.crawler.Crawler;
import cn.vfire.web.collector2.event.CrawlerEvent;
import cn.vfire.web.collector2.model.CrawlDatum;
import cn.vfire.web.collector2.model.Page;
import cn.vfire.web.collector2.tools.DefaultExecutor;
import cn.vfire.web.collector2.tools.Executor;
import cn.vfire.web.collector2.tools.TaskPool;

public class CrawlerTest {

	public static void main(String[] args) {

		CrawlerTest test = new CrawlerTest();

		CrawlerConfig config = test.getConfig("crawlerJob");

		Executor executor = test.getExecutor();

		TaskPool taskPool = test.getTaskPool(config);

		Crawler crawler = new Crawler(config);
		crawler.addEvent(event);
		crawler.setExecutor(executor);
		crawler.setTaskPool(taskPool);

		crawler.start();

	}


	private Executor getExecutor() {
		Executor executor = new DefaultExecutor();
		return executor;
	}


	private TaskPool getTaskPool(CrawlerConfig config) {

		try {

			TaskPool taskPool = new TaskPool(config) {

				@Override
				protected List<CrawlDatum> getList(int size) {
					return null;
				}
			};

			String[] ruls = new String[] { "http://www.biquge.la/book/14/9609.html",
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

			for (String url : ruls) {
				taskPool.add(new CrawlDatum(url));
			}

			return taskPool;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	private CrawlerConfig getConfig(String name) {

		try {
			CrawlerXmlFactory xmlTool = CrawlerXmlFactory.getCrawlerXmlTool();

			String filePath = ClassLoader
					.getSystemResource("cn/vfire/web/collector/tools/crawler/xml/crawler-config.xml").getFile();

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
