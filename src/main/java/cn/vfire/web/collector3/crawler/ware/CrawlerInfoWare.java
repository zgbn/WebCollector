package cn.vfire.web.collector3.crawler.ware;

import java.util.List;

import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.crawler.format.FormatData;
import cn.vfire.web.collector3.tools.crawler.proxyip.ProxyIpPool;

public interface CrawlerInfoWare extends Ware {

	public void setName(String name);

	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo);

	public void setProxyIpPool(ProxyIpPool proxyIpPool);

	public void setDataModes(List<DataMode> dataModes);

	public void setRegexRules(List<String> regex);

	public void setUnregexRules(List<String> regex);

	public void setFormatData(FormatData formatData);

	public void setSeedurl(String seedUrl);

	public void setSnapshot(int time, int size);

}
