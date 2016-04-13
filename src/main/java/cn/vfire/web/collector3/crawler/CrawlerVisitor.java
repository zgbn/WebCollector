package cn.vfire.web.collector3.crawler;

import java.util.List;

import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.ResultData;
import cn.vfire.web.collector3.net.HttpResponse;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.pool.ProxyIpPool;
import cn.vfire.web.collector3.tools.pool.TaskPool;

public interface CrawlerVisitor {

	/**
	 * 从Page中提取数据
	 * 
	 * @param page
	 */
	public List<ResultData> fetchResultData(Page page);


	public void fetchCrawlDatum(Page page, TaskPool taskPool);


	public HttpResponse getHttpResponse();


	public void setProxyIpPool(ProxyIpPool proxyIpPool);


	public void setDataMode(List<DataMode> dataMode);
}
