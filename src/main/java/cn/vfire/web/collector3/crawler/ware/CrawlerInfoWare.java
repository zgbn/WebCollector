package cn.vfire.web.collector3.crawler.ware;

import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;

public interface CrawlerInfoWare extends Ware {

	public void setName(String name);


	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo);

}
