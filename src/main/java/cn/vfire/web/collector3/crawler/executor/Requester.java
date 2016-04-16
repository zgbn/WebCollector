package cn.vfire.web.collector3.crawler.executor;

import cn.vfire.web.collector3.lang.CrawlerNetException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.net.HttpRequest;
import cn.vfire.web.collector3.net.HttpResponse;

public interface Requester {

	public HttpResponse setResponseAndGet(HttpResponse response, CrawlDatum crawlDatum) throws CrawlerNetException;


	public HttpRequest setRequestAndGet(HttpRequest request, CrawlDatum crawlDatum) throws CrawlerNetException;
}
