package cn.vfire.web.collector3.crawler.executor;

import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.crawler.ware.RequesterWare;
import cn.vfire.web.collector3.lang.CrawlerNetException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.net.HttpRequest;
import cn.vfire.web.collector3.net.HttpResponse;

public final class DefaultExecutor implements Executor, Requester, RequesterWare, Default {

	private Requester requester;


	@Override
	public Page execute(CrawlDatum crawlDatum) throws CrawlerNetException {

		long timeConsuming = System.currentTimeMillis();

		Page page = this.defaultExe(crawlDatum);

		timeConsuming = System.currentTimeMillis() - timeConsuming;

		page.setResponseTime(timeConsuming);

		return page;

	}


	private Page defaultExe(CrawlDatum crawlDatum) throws CrawlerNetException {

		this.requester = this.requester == null ? this : this.requester;

		HttpResponse response = null;

		HttpRequest request = null;

		try {

			if (crawlDatum.isProxy()) {
				request = new HttpRequest(crawlDatum, crawlDatum.getProxy());
			}
			else {
				request = new HttpRequest(crawlDatum);
			}

			request = this.requester.setRequestAndGet(request, crawlDatum);

			response = this.requester.setResponseAndGet(request.getResponse(), crawlDatum);

			Page page = new Page(crawlDatum, response);

			return page;

		}
		catch (Exception e) {

			throw new CrawlerNetException(
					CrawlerExpInfo.NET.setInfo("URL={} Proxy={}", crawlDatum.getUrl(), crawlDatum.getProxy()), e);

		}

	}


	@Override
	public HttpResponse setResponseAndGet(HttpResponse response, CrawlDatum crawlDatum) throws CrawlerNetException {
		return response;
	}


	@Override
	public HttpRequest setRequestAndGet(HttpRequest request, CrawlDatum crawlDatum) throws CrawlerNetException {
		return request;
	}


	@Override
	public void setRequester(Requester requester) {
		this.requester = requester;
	}

}
