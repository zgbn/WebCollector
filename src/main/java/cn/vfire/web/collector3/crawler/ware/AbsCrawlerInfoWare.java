package cn.vfire.web.collector3.crawler.ware;

import java.util.List;

import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.crawler.format.FormatData;
import cn.vfire.web.collector3.tools.crawler.proxyip.ProxyIpPool;

public abstract class AbsCrawlerInfoWare implements CrawlerInfoWare {

	protected String name;

	protected CrawlerAttrInfo crawlerAttrInfo;

	protected ProxyIpPool proxyIpPool;

	protected List<DataMode> dataModes;

	protected List<String> regexRules;

	protected List<String> unregexRules;

	protected FormatData formatData;

	protected String seedUrl;

	protected int snapshotTime;

	protected int snapshotSize;

	@Override
	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo) {
		this.crawlerAttrInfo = crawlerAttrInfo;
	}

	@Override
	public void setDataModes(List<DataMode> dataModes) {
		this.dataModes = dataModes;
	}

	@Override
	public void setFormatData(FormatData formatData) {
		this.formatData = formatData;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setProxyIpPool(ProxyIpPool proxyIpPool) {
		this.proxyIpPool = proxyIpPool;
	}

	@Override
	public void setRegexRules(List<String> regex) {
		this.regexRules = regex;
	}

	@Override
	public void setSeedurl(String seedUrl) {
		this.seedUrl = seedUrl;
	}

	@Override
	public void setUnregexRules(List<String> unregex) {
		this.unregexRules = unregex;
	}

	@Override
	public void setSnapshot(int time, int size) {
		this.snapshotTime = time;
		this.snapshotSize = size;
	}

}
