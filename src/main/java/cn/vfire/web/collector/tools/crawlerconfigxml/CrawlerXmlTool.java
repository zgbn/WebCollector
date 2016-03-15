package cn.vfire.web.collector.tools.crawlerconfigxml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.element.Crawlerconfigs;
import cn.vfire.web.collector.tools.crawlerconfigxml.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector.tools.crawlerconfigxml.lang.CrawlerXmlFile;

import com.google.gson.Gson;

public final class CrawlerXmlTool {

	private static final Logger log = LoggerFactory.getLogger(CrawlerXmlTool.class);

	private static CrawlerXmlTool XmlTool;

	private DocumentBuilder docBuiler;

	private Crawlerconfigs crawlerConfigs = new Crawlerconfigs();

	synchronized public static CrawlerXmlTool getCrawlerXmlTool() throws ParserConfigurationException {
		if (XmlTool == null) {
			XmlTool = new CrawlerXmlTool();
		}
		return XmlTool;
	}

	public static void main(String[] args) throws Exception {

		CrawlerXmlTool xmlTool = CrawlerXmlTool.getCrawlerXmlTool();

		String filePath = ClassLoader.getSystemResource("cn/vfire/web/collector/tools/crawlerconfigxml/xml/crawler-config.xml").getFile();

		log.info("解析文件物理位置{}。", filePath);

		File xml = new File(filePath);

		log.info("开始解析CrawlerXml配置文件{}。", xml.getPath());

		xmlTool.parse(xml);

		log.info("解析完成：：：：crawlerConfigs:{}", xmlTool.crawlerConfigs);
		log.info("解析完成：：：：crawlerConfigs.size:{}", xmlTool.crawlerConfigs.size());
		log.info("解析完成：：：：crawlerConfigs.getFormatclass:{}", xmlTool.crawlerConfigs.getFormatclass());
		log.info("解析完成：：：：crawlerConfigs.getCrawlerconfig:{}", xmlTool.crawlerConfigs.getCrawlerconfig());
		log.info("解析完成：：：：crawlerConfigs.getOutfile:{}", xmlTool.crawlerConfigs.getOutfile());
		log.info("解析完成：：：：crawlerConfigs.getOutdata:{}", xmlTool.crawlerConfigs.getOutdata());

	}

	private CrawlerXmlTool() throws ParserConfigurationException {
		this.docBuiler = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}

	public void parse(File... crawlerConfigXml) throws Exception {

		int length = crawlerConfigXml.length;

		for (int i = 0; i < length; i++) {

			try {

				CrawlerXmlFile xmlfile = new CrawlerXmlFile(crawlerConfigXml[i]);

				this.parseXmlFile(xmlfile);

			} catch (IOException e) {

				throw new CrawlerConfigXmlException(e, "处理文件内容大小写发生异常。File：%s", crawlerConfigXml[i].getPath());

			}

		}
	}

	private void parseXmlFile(CrawlerXmlFile xmlfile) throws Exception {

		Document document = this.docBuiler.parse(xmlfile.getTmpCrawlerConfigXml());

		Node rootNode = document.getFirstChild();
		// TODO 改完了
		this.crawlerConfigs.add(new Crawlerconfigs().parse(rootNode));

		if (this.crawlerConfigs.size() == 1) {
			this.crawlerConfigs = (Crawlerconfigs) this.crawlerConfigs.get(0);
		}

	}

}
