package cn.vfire.web.collector3.crawler.defaults;

import cn.vfire.web.collector3.crawler.executor.Executor;
import cn.vfire.web.collector3.crawler.executor.Requester;
import cn.vfire.web.collector3.crawler.ware.AbsSuperWare;
import cn.vfire.web.collector3.lang.CrawlerNetException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.net.HttpRequest;
import cn.vfire.web.collector3.net.HttpResponse;

/**
 * 
 * @author ChenGang
 *
 */
public final class DefaultExecutor extends AbsSuperWare implements Executor, Requester, Default {

	private Page defaultExe(CrawlDatum crawlDatum) throws CrawlerNetException {

		this.requester = this.requester == null ? this : this.requester;

		Page page = new Page(crawlDatum, this.requester.getResponse(crawlDatum));

		return page;

	}

	@Override
	public Page execute(CrawlDatum crawlDatum) throws CrawlerNetException {

		long timeConsuming = System.currentTimeMillis();

		Page page = this.defaultExe(crawlDatum);

		timeConsuming = System.currentTimeMillis() - timeConsuming;

		page.setResponseTime(timeConsuming);

		return page;

	}

	@Override
	public HttpResponse getResponse(CrawlDatum crawlDatum) throws CrawlerNetException {

		HttpRequest request = null;

		try {

			if (crawlDatum.isProxy()) {
				request = new HttpRequest(crawlDatum, crawlDatum.getProxy());
			} else {
				request = new HttpRequest(crawlDatum);
			}

			return request.getResponse();

		} catch (Exception e) {
			throw new CrawlerNetException(CrawlerExpInfo.NET.setInfo("URL={} Proxy={}", crawlDatum.getUrl(), crawlDatum.getProxy()), e);
		}
	}

}
