package cn.vfire.web.collector.tools.crawler;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrawlerXmlFactoryTest {

	@Test
	public void testMain() {

		try {
			CrawlerXmlFactory xmlTool = CrawlerXmlFactory.getCrawlerXmlTool();
			String filePath = ClassLoader
					.getSystemResource("cn/vfire/web/collector/tools/crawler/xml/crawler-config.xml").getFile();

			log.info("解析文件物理位置{}。", filePath);

			File xml = new File(filePath);

			log.info("开始解析CrawlerXml配置文件{}。", xml.getPath());

			xmlTool.parseXmlFile(xml);

			log.info("Crawler任务id:{}", xmlTool.getCrawlerIds());

			log.info("Crawler任务配置详细描述:{}", xmlTool.getCrawlerForJson("crawlerJob"));

			log.info("Crawler任务配置Datamode描述:{}", xmlTool.getCrawler("crawlerJob").getDatamode());

			log.info("Crawler任务配置Datamode.ref描述:{}", xmlTool.getCrawler("crawlerJob").getDatamode().get(0).getRef());

			log.info("Crawler任务配置Datamode.ref描述:{}",
					xmlTool.getCrawler("crawlerJob").getDatamode().get(0).getOutdata());
			log.info("Crawler任务配置Datamode.ref描述:{}",
					xmlTool.getCrawler("crawlerJob").getDatamode().get(1).getOutdata());
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
