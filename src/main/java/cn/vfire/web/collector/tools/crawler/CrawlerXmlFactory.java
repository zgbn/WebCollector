package cn.vfire.web.collector.tools.crawler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.vfire.common.utils.json.DefaultJson;
import cn.vfire.web.collector.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector.tools.crawler.element.Crawlerconfigs;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerXmlFile;

public final class CrawlerXmlFactory {

	private static CrawlerXmlFactory XmlTool;

	private static final String Prefix = "crawler-config";

	private DocumentBuilder docBuiler;

	private DefaultJson jsonTools = new DefaultJson();

	private Crawlerconfigs crawlerConfigs;


	synchronized public static CrawlerXmlFactory getCrawlerXmlTool() throws ParserConfigurationException {
		if (XmlTool == null) {
			XmlTool = new CrawlerXmlFactory();
		}
		return XmlTool;
	}


	synchronized public static CrawlerXmlFactory getCrawlerXmlTool(File... crawlerConfigXml) throws Exception {
		if (XmlTool == null) {
			XmlTool = new CrawlerXmlFactory();
			XmlTool.parseXmlFile(crawlerConfigXml);
		}
		return XmlTool;
	}


	synchronized public static CrawlerXmlFactory getCrawlerXmlTool(String xmlFilePath) throws Exception {
		File[] files = loadCrawlerXmlFile(xmlFilePath);
		if (XmlTool == null) {
			XmlTool = new CrawlerXmlFactory();
			XmlTool.parseXmlFile(files);
		}
		return XmlTool;
	}


	private static File[] loadCrawlerXmlFile(String xmlPath) throws CrawlerConfigXmlException {

		xmlPath = xmlPath == null ? CrawlerXmlFactory.class.getResource("/").getPath() : xmlPath;

		File xmlFile = new File(xmlPath);

		if (xmlFile.exists()) {

			boolean flag = false;

			final Pattern pattern = Pattern.compile("^" + Prefix + "[^\\/:\\*\\?\"\\<\\>\\|]*?\\.xml$");

			if (xmlFile.isFile()) {
				String xmlFileName = xmlFile.getName();
				flag = pattern.matcher(xmlFileName).find();
				if (flag) { return new File[] { xmlFile }; }
			}
			else if (xmlFile.isDirectory()) {
				File[] list = xmlFile.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						String xmlFileName = pathname.getName();
						return pattern.matcher(xmlFileName).find();
					}
				});

				if (list != null && list.length > 0) {
					flag = true;
					return list;
				}
			}
		}

		throw new CrawlerConfigXmlException("在xmlPath中没有找到Crawler的任务配置文件,该配置文件名必须以%s开头的xml文件。xmlPath:%s", Prefix,
				xmlPath);
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

			}
			catch (IOException e) {

				throw new CrawlerConfigXmlException(e, "处理文件内容大小写发生异常。File：%s", crawlerConfigXml[i].getPath());

			}

		}

		this.setElementRefs();

		return this.crawlerConfigs.getCrawlerIds();
	}


	public void setElementRefs() {

		Set<String> keySey = this.crawlerConfigs.getCacheElementMap().keySet();
		for (String key : keySey) {

			Element<?> element = this.crawlerConfigs.getCacheElementMap().get(key);

			for(String ref : element.getRef()){
				
				Element<?> refElement = this.crawlerConfigs.getCacheElementMap().get(ref) ;
				
				element.setRefElement(refElement);
				
			}
		}
	}


	public String getCrawlerForJson(String crawlerId) {
		CrawlerConfig crawler = this.getCrawler(crawlerId);
		if (crawler != null) { return this.jsonTools.toJson(crawler); }
		return null;
	}


	public CrawlerConfig getCrawler(String crawlerId) {
		Element<?> element = this.crawlerConfigs.getCacheElementMap().get(crawlerId);
		if (element instanceof CrawlerConfig) { return (CrawlerConfig) element; }
		return null;
	}


	/**
	 * @param xmlfile
	 * @throws Exception
	 */
	private Crawlerconfigs parse(CrawlerXmlFile xmlfile) throws Exception {

		Document document = this.docBuiler.parse(xmlfile.getTmpCrawlerConfigXml());

		Node rootNode = document.getFirstChild();

		this.crawlerConfigs = new Crawlerconfigs();

		this.crawlerConfigs.parse(rootNode);

		return this.crawlerConfigs;

	}


	public List<String> getCrawlerIds() {
		List<String> list = this.crawlerConfigs.getCrawlerIds();
		return list;
	}


	public void setJsonTools(Object json) {
		this.jsonTools.setJson(json);
	}

}
