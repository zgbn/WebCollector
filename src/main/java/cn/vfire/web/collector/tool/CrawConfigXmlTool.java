package cn.vfire.web.collector.tool;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class CrawConfigXmlTool {

	private static DocumentBuilder DocBuiler;

	private static XPath XPath = XPathFactory.newInstance().newXPath();

	private static String XPath_formatclass = "/crawlerconfigs/formatclass";

	private static String XPath_crawlerConfig = "/crawlerconfigs/crawlerconfig";

	private static String XPath_seedUrl = "/crawlerconfigs/crawlerconfig/seedurl";

	private static String XPath_threads = "/crawlerconfigs/crawlerconfig/threads";

	private static String XPath_minThreads = "/crawlerconfigs/crawlerconfig/minThreads";

	private static String XPath_incThreads = "/crawlerconfigs/crawlerconfig/incThreads";

	private static String XPath_maxThreads = "/crawlerconfigs/crawlerconfig/maxThreads";

	private static String XPath_topNum = "/crawlerconfigs/crawlerconfig/topNum";

	private static String XPath_retry = "/crawlerconfigs/crawlerconfig/retry";

	private static String XPath_maxExecuteCount = "/crawlerconfigs/crawlerconfig/maxExecuteCount";

	private static String XPath_isProxy = "/crawlerconfigs/crawlerconfig/isProxy";

	private static String XPath_proxyIp = "/crawlerconfigs/crawlerconfig/proxyIp";

	private static String XPath_regexRules = "/crawlerconfigs/crawlerconfig/regexRules";

	private static String XPath_regexRules_regex = "/crawlerconfigs/crawlerconfig/regexRules/regex";

	private static String XPath_unregexRules_regex = "/crawlerconfigs/crawlerconfig/unregexRules/regex";

	private static String XPath_dataMode = "/crawlerconfigs/crawlerconfig/dataMode";

	private static String XPath_Node = "/crawlerconfigs/crawlerconfig/dataMode/Node";

	private static String XPath_outData = "/crawlerconfigs/crawlerconfig/outData";

	private static String XPath_outData_dataMode = "/crawlerconfigs/crawlerconfig/outData/dataMode";

	private static String XPath_outData_formatClass = "/crawlerconfigs/crawlerconfig/outData/formatClass";

	private static String XPath_outData_outFile = "/crawlerconfigs/crawlerconfig/outData/outFile";

	private static String XPath_outData_prefix = "/crawlerconfigs/crawlerconfig/outData/outFile/prefix";

	private static String XPath_outData_suffix = "/crawlerconfigs/crawlerconfig/outData/outFile/suffix";

	private static String XPath_outData_path = "/crawlerconfigs/crawlerconfig/outData/outFile/path";

	private static String XPath_outData_name = "/crawlerconfigs/crawlerconfig/outData/outFile/name";

	private static String getChildRelativeXPath(String parentXPath, String childXPath) {
		if(){
			
		}
	}

	public CrawConfigXmlTool() throws ParserConfigurationException {
		if (DocBuiler == null) {
			DocBuiler = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
	}

}
