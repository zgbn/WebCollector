package cn.vfire.web.collector3.tools.crawler.enums;

import cn.vfire.web.collector3.tools.crawler.Element;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfigs;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.crawler.element.FormatClass;
import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;
import cn.vfire.web.collector3.tools.crawler.element.OutData;
import cn.vfire.web.collector3.tools.crawler.element.OutFile;
import cn.vfire.web.collector3.tools.crawler.element.ProxyIP;
import cn.vfire.web.collector3.tools.crawler.element.RegexRules;
import cn.vfire.web.collector3.tools.crawler.element.UnregexRules;
import cn.vfire.web.collector3.tools.crawler.element.Urls;

public enum BeanEnum {

	list(NList.class), node(NNode.class), urls(Urls.class), datamode(DataMode.class), unregexrules(UnregexRules.class), regexrules(RegexRules.class), crawlerconfig(
			CrawlerConfig.class), outfile(OutFile.class), outdata(OutData.class), proxyip(ProxyIP.class), formatclass(FormatClass.class), crawlerconfigs(
			CrawlerConfigs.class);

	private Class<? extends Element<?>> beanClz;

	private BeanEnum(Class<? extends Element<?>> beanClz) {
		this.beanClz = beanClz;
	}

	public Element<?> newBean() {
		try {
			Element<?> bean = beanClz.newInstance();
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
