package cn.vfire.web.collector3.crawler.executor;

import cn.vfire.web.collector3.lang.CrawlerNetException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.net.HttpResponse;

public interface Requester {

	public HttpResponse getResponse(CrawlDatum crawlDatum) throws CrawlerNetException;

}
