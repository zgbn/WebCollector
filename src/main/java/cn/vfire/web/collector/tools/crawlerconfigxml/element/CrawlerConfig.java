package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

/**
 * 爬虫任务配置
 * 
 * @author ChenGang
 *
 */
public class CrawlerConfig extends Element {

	private static final long serialVersionUID = 1L;

	@Getter
	private Name name;

	@Getter
	private Description description;

	@Getter
	private SeedUrl seedurl;

	@Getter
	private int threads = 5;

	@Getter
	private int incThreads = 5;

	@Getter
	private int minThreads = 5;

	@Getter
	private int maxThreads = 50;

	@Getter
	private int topNum = -1;

	@Getter
	private int retry = 1;

	@Getter
	private int depth = 1;

	@Getter
	private int maxExecuteCount = 5;

	@Getter
	private boolean isProxy = false;

	@Getter
	private ProxyIP proxyIp;

	@Getter
	private RegexRules regexRules;

	@Getter
	private UnregexRules unregexRules;

	@Getter
	private DataMode dataModes;

	@Override
	protected String[] parseChildNodeName() {
		return new String[] { "name", "description", "seedurl", "proxyip", "regexrules", "unregexrules", "datamode" };
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {

	}

}
