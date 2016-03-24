package cn.vfire.web.collector.tools.crawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.element.Crawlerconfigs;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerXmlFile;

public final class CrawlerXmlFactory {

	private static final Logger log = LoggerFactory.getLogger(CrawlerXmlFactory.class);

	protected static final String REF = "ref";

	private static List<String> crawlerId = new ArrayList<String>();

	private static CrawlerXmlFactory XmlTool;

	private DocumentBuilder docBuiler;

	private Crawlerconfigs crawlerConfigs = new Crawlerconfigs();

	synchronized public static CrawlerXmlFactory getCrawlerXmlTool() throws ParserConfigurationException {
		if (XmlTool == null) {
			XmlTool = new CrawlerXmlFactory();
		}
		return XmlTool;
	}

	public static void main(String[] args) throws Exception {

		CrawlerXmlFactory xmlTool = CrawlerXmlFactory.getCrawlerXmlTool();
		String filePath = ClassLoader.getSystemResource("cn/vfire/web/collector/tools/crawler/xml/crawler-config.xml").getFile();

		log.info("解析文件物理位置{}。", filePath);

		File xml = new File(filePath);

		log.info("开始解析CrawlerXml配置文件{}。", xml.getPath());

		List<String> list = xmlTool.parseXmlFile(xml);

		log.info("Crawler任务id:{}", list);

	}

	private CrawlerXmlFactory() throws ParserConfigurationException {
		this.docBuiler = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}

	public List<String> parseXmlFile(File... crawlerConfigXml) throws Exception {

		int length = crawlerConfigXml.length;

		for (int i = 0; i < length; i++) {

			try {

				CrawlerXmlFile xmlfile = new CrawlerXmlFile(crawlerConfigXml[i]);

				this.parse(xmlfile);

			} catch (IOException e) {

				throw new CrawlerConfigXmlException(e, "处理文件内容大小写发生异常。File：%s", crawlerConfigXml[i].getPath());

			}

		}

		return Collections.unmodifiableList(crawlerId);
	}

	/**
	 * @param xmlfile
	 * @throws Exception
	 */
	private void parse(CrawlerXmlFile xmlfile) throws Exception {

		Document document = this.docBuiler.parse(xmlfile.getTmpCrawlerConfigXml());

		Node rootNode = document.getFirstChild();

		this.crawlerConfigs = new Crawlerconfigs();

		this.crawlerConfigs.parse(rootNode);

	}

}
