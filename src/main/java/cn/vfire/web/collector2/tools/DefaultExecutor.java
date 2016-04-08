package cn.vfire.web.collector2.tools;

import cn.vfire.web.collector2.lang.CrawlerNetException;
import cn.vfire.web.collector2.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector2.model.CrawlDatum;
import cn.vfire.web.collector2.model.Page;
import cn.vfire.web.collector2.net.HttpRequest;
import cn.vfire.web.collector2.net.HttpResponse;

public final class DefaultExecutor implements Executor {

	private Executor executor;


	public DefaultExecutor() {
	}


	public DefaultExecutor(Executor executor) {
		this.executor = executor;
	}


	@Override
	public Page execute(CrawlDatum crawlDatum) throws CrawlerNetException {

		long timeConsuming = System.currentTimeMillis();

		Page page = null;

		if (this.executor != null) {
			page = this.executor.execute(crawlDatum);
		}
		else {
			page = this.defaultExe(crawlDatum);
		}

		timeConsuming = System.currentTimeMillis() - timeConsuming;

		page.setResponseTime(timeConsuming);

		return page;

	}


	private Page defaultExe(CrawlDatum crawlDatum) throws CrawlerNetException {
		try {
			HttpRequest request = null;

			if (crawlDatum.isProxy()) {
				request = new HttpRequest(crawlDatum, crawlDatum.getProxy());
			}
			else {
				request = new HttpRequest(crawlDatum);
			}

			HttpResponse response = request.getResponse();

			Page page = new Page(crawlDatum, response);

			return page;
		}
		catch (Exception e) {
			throw new CrawlerNetException(
					CrawlerExpInfo.NET.setInfo("URL={} Proxy={}", crawlDatum.getUrl(), crawlDatum.getProxy()), e);
		}
	}


	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

}
