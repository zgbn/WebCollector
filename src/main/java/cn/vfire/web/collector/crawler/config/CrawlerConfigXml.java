package cn.vfire.web.collector.crawler.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lombok.Getter;
import lombok.Setter;
import cn.vfire.web.collector.net.ProxyIP;

/**
 * 用于解析爬虫的属性。 默认读取名字为 crawler-config.xml配置文件构造配置对象。
 * 
 * @author ChenGang
 *
 */
public class CrawlerConfigXml implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 任务ID */
	@Getter
	@Setter
	private String id;

	/**
	 * 采集种子URL
	 */
	@Getter
	@Setter
	private String seedUrl;

	/** 是否使用代理服务器采集 */
	@Getter
	@Setter
	private boolean isProxy;

	/** 代理服务器IP */
	@Getter
	@Setter
	private List<ProxyIP> proxyIp = new LinkedList<ProxyIP>();

	/** 提取种子页面中URL的正则 */
	@Getter
	@Setter
	private List<String> regexRules = new LinkedList<String>();

	/** 屏蔽种子页面中URL的正则 */
	@Getter
	@Setter
	private List<String> unregexRules = new LinkedList<String>();

	/** 控制爬虫的稳定线程数量 */
	@Getter
	@Setter
	private int threads = 1;

	/** 递增线程数个数 */
	@Getter
	@Setter
	private int incThreads = 5;

	/** 控制爬虫的线程最小数量 */
	@Getter
	@Setter
	private int minThreads = 1;

	/** 控制爬虫的线程最大数量 */
	@Getter
	@Setter
	private int maxThreads = 10;

	/** 控制提取爬去页面中满足子任务URL的数量，如果topNum>0代表从抓去页面解析出来的子任务URL中一次提取到topNum后停止。 */
	@Getter
	@Setter
	private int topNum = -1;

	/** 当采集URL发生异常时，在当前线程任务中重新尝试采集次数，默认为1次 */
	@Getter
	@Setter
	private int retry = 1;

	/**
	 * 当采集URL发生异常时，当前线程会将任务持久化，追加到总任务最后面。此处用来控制该任务最大被执行几次。当
	 * maxExecuteCount<=0的时候，发生异常的任务不会追加总任务池中。
	 */
	@Getter
	@Setter
	private int maxExecuteCount = -1;

	/** 数据提取映射，该对象保存着提取数据的key=selecter的映射关系。selecter为css的选择器。 */
	@Getter
	@Setter
	private Map<String, String> dataSelecterMapping = new HashMap<String, String>();

	/**
	 * 配置文件
	 */
	@Getter
	@Setter
	private String crawlerConfigXml;

	/**
	 * 提取数据模型
	 */
	@Getter
	@Setter
	private DataMode dataMode;

	@Getter
	@Setter
	private OutFile outFile;

	public CrawlerConfigXml() {
	}

	/**
	 * 通过配置文件创建配置对象
	 * 
	 * @param crawlerConfigXml
	 */
	public CrawlerConfigXml(String crawlerConfigXml) {
		this.crawlerConfigXml = crawlerConfigXml;
	}

	private void readCrawlerConfigXml() {

		System.out.println(this.crawlerConfigXml);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		try {

			InputStream ins = this.getClass().getResourceAsStream(this.crawlerConfigXml);

			DocumentBuilder docBuider = docFactory.newDocumentBuilder();

			Document doc = docBuider.parse(ins);

			NodeList nodeList = doc.getElementsByTagName("formatClass");

			System.out.println(nodeList.getLength());

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		String crawler_config = "/cn/vfire/web/collector/crawler/config/crawler-config.xml";
		CrawlerConfigXml xml = new CrawlerConfigXml(crawler_config);
		xml.readCrawlerConfigXml();
	}

	/**
	 * 设置HTTP代理服务器IP。 格式 IP:port。
	 * 
	 * @param ip
	 */
	public void setProxyIp(String ip) {
		String[] pp = ip.split(":");
		ProxyIP proxyIp = new ProxyIP(pp[0], pp[1]);
		this.proxyIp.add(proxyIp);
	}

	/**
	 * 设置HTTP代理服务器IP。 格式 IP:port。
	 * 
	 * @param ips
	 */
	public void setProxyIp(String... ips) {
		for (String ip : ips) {
			this.setProxyIp(ip);
		}
	}

}
