package cn.vfire.web.collector3.crawler.visitor;

import java.util.LinkedList;
import java.util.List;

import cn.vfire.web.collector3.crawler.CrawlerVisitor;
import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.ResultData;
import cn.vfire.web.collector3.net.HttpResponse;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;
import cn.vfire.web.collector3.tools.pool.ProxyIpPool;
import cn.vfire.web.collector3.tools.pool.TaskPool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultCrawlerVisitor implements CrawlerVisitor, Default {

	private List<DataMode> dataModes;


	@Override
	public List<ResultData> fetchResultData(Page page) {

		List<ResultData> resultDatas = new LinkedList<ResultData>();

		for (DataMode datamode : dataModes) {

			ResultData resultData = new ResultData();

			List<String> regexs = datamode.getUrls().getRegex();

			for (String regex : regexs) {

				if (page.matchUrl(regex)) {

					for (NNode nnode : datamode.getNode()) {
						resultData.addRNode(page, nnode);
					}

					for (NList nlist : datamode.getList()) {
						resultData.addRList(page, nlist);
					}

					resultDatas.add(resultData);

					break;

				}
			}
		}

		return resultDatas;
	}


	@Override
	public void fetchCrawlDatum(Page page, TaskPool taskPool) {
		// TODO Auto-generated method stub
		log.info("TODO...");
	}


	@Override
	public HttpResponse getHttpResponse() {
		// TODO Auto-generated method stub
		log.info("TODO...");
		return null;
	}


	@Override
	public void setProxyIpPool(ProxyIpPool proxyIpPool) {
		// TODO Auto-generated method stub
		log.info("TODO...");
	}


	@Override
	public void setDataMode(List<DataMode> dataMode) {
		this.dataModes = dataMode;
	}

}
