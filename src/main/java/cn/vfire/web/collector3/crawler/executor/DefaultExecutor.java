package cn.vfire.web.collector3.crawler.executor;

import lombok.Setter;
import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.lang.CrawlerNetException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.net.HttpRequest;
import cn.vfire.web.collector3.net.HttpResponse;
import cn.vfire.web.collector3.tools.executor.Executor;
import cn.vfire.web.collector3.tools.executor.Requester;

public final class DefaultExecutor implements Executor, Requester, Default {

	@Setter
	private Requester requester;

	public DefaultExecutor() {
	}

	@Override
	public Page execute(CrawlDatum crawlDatum) throws CrawlerNetException {

		long timeConsuming = System.currentTimeMillis();

		Page page = this.defaultExe(crawlDatum);

		timeConsuming = System.currentTimeMillis() - timeConsuming;

		page.setResponseTime(timeConsuming);

		return page;

	}

	private Page defaultExe(CrawlDatum crawlDatum) throws CrawlerNetException {

		HttpResponse response = null;

		HttpRequest request = null;

		try {

			if (crawlDatum.isProxy()) {
				request = new HttpRequest(crawlDatum, crawlDatum.getProxy());
			} else {
				request = new HttpRequest(crawlDatum);
			}

			request = this.getRequester().setRequestAndGet(request, crawlDatum);

			response = this.getRequester().setResponseAndGet(request.getResponse(), crawlDatum);

			Page page = new Page(crawlDatum, response);

			return page;

		} catch (Exception e) {

			throw new CrawlerNetException(CrawlerExpInfo.NET.setInfo("URL={} Proxy={}", crawlDatum.getUrl(), crawlDatum.getProxy()), e);

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

	public Requester getRequester() {
		return requester == null ? this : requester;
	}


}
