package cn.vfire.web.collector3.crawler.visitor;

import java.util.List;

import cn.vfire.web.collector3.crawler.CrawlerVisitor;
import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.ResultData;
import cn.vfire.web.collector3.net.HttpResponse;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.pool.ProxyIpPool;
import cn.vfire.web.collector3.tools.pool.TaskPool;
import lombok.Getter;

public final class ProxyCrawlerVisitor implements CrawlerVisitor {

	@Getter
	private CrawlerVisitor visitor = new DefaultCrawlerVisitor();


	@Override
	public void fetchCrawlDatum(Page page, TaskPool taskPool) {
		this.visitor.fetchCrawlDatum(page, taskPool);
	}


	@Override
	public List<ResultData> fetchResultData(Page page) {
		return this.visitor.fetchResultData(page);
	}


	@Override
	public HttpResponse getHttpResponse() {
		return this.visitor.getHttpResponse();
	}


	@Override
	public void setProxyIpPool(ProxyIpPool proxyIpPool) {
		this.visitor.setProxyIpPool(proxyIpPool);
	}


	public void setVisitor(CrawlerVisitor visitor) {
		if (visitor instanceof Default) {
			this.visitor = new DefaultCrawlerVisitor();
		}
		else {
			this.visitor = visitor;
		}
	}


	@Override
	public void setDataMode(List<DataMode> dataMode) {
		this.visitor.setDataMode(dataMode);
	}

}
