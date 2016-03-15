package cn.vfire.web.collector.tools.crawlerconfigxml.enums;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.CrawlerConfig;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.Crawlerconfigs;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.DataMode;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.Description;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.FormatClass;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.NList;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.Name;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.NNode;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.OutData;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.OutFile;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.ProxyIP;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.RegexRules;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.SeedUrl;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.UnregexRules;

public enum BeanEnum {

	list(NList.class),node(NNode.class), datamode(DataMode.class), unregexrules(UnregexRules.class), regexrules(RegexRules.class), proxyip(ProxyIP.class), seedurl(SeedUrl.class), outfile(
			OutFile.class), outdata(OutData.class), description(Description.class), name(Name.class), crawlerconfigs(Crawlerconfigs.class), formatclass(
			FormatClass.class), crawlerconfig(CrawlerConfig.class);

	private Class<? extends Element> beanClz;

	private BeanEnum(Class<? extends Element> beanClz) {
		this.beanClz = beanClz;
	}

	public Element newBean() {

		try {
			Element bean = beanClz.newInstance();
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
