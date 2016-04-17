package cn.vfire.web.collector3.crawler.executor;

import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.crawler.ware.RequesterWare;
import cn.vfire.web.collector3.lang.CrawlerNetException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.net.HttpRequest;
import cn.vfire.web.collector3.net.HttpResponse;
import cn.vfire.web.collector3.tools.crawler.element.ProxyIP;

/**
 * XXX DefaultExecutor实现还需要优化
 * 
 * @author ChenGang
 *
 */
public final class DefaultExecutor implements Executor, Requester, RequesterWare, CrawlerInfoWare, Default {

	private Requester requester;

	@SuppressWarnings("unused")
	private String name;

	@SuppressWarnings("unused")
	private CrawlerAttrInfo crawlerAttrInfo;

	@SuppressWarnings("unused")
	private ProxyIP proxyIPs;


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
			}
			else {
				request = new HttpRequest(crawlDatum);
			}

			return request.getResponse();

		}
		catch (Exception e) {
			throw new CrawlerNetException(
					CrawlerExpInfo.NET.setInfo("URL={} Proxy={}", crawlDatum.getUrl(), crawlDatum.getProxy()), e);
		}
	}


	@Override
	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo) {
		this.crawlerAttrInfo = crawlerAttrInfo;

	}


	@Override
	public void setName(String name) {
		this.name = name;
	}


	@Override
	public void setProxyIPs(ProxyIP proxyIPs) {
		this.proxyIPs = proxyIPs;
	}


	@Override
	public void setRequester(Requester requester) {
		this.requester = requester;
	}

}
