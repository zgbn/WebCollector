package cn.vfire.web.collector3.crawler.visitor;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Document;

import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.crawler.executor.Requester;
import cn.vfire.web.collector3.crawler.pool.TaskPool;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.crawler.ware.RequesterWare;
import cn.vfire.web.collector3.crawler.ware.TaskPoolWare;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.model.Links;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.ResultData;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;
import cn.vfire.web.collector3.tools.crawler.element.ProxyIP;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultCrawlerVisitor
		implements CrawlerVisitor, CrawlerInfoWare, TaskPoolWare, RequesterWare, Default {

	private List<DataMode> dataModes;


	private TaskPool taskPool;


	private String name;


	private CrawlerAttrInfo crawlerAttrInfo;


	private ProxyIP proxyIPs;


	@Override
	public void fetchCrawlDatum(Page page, TaskPool taskPool) {

		String conteType = page.getResponse().getContentType();

		if (conteType != null && conteType.contains("text/html")) {

			Document doc = page.doc();

			if (doc != null) {

				for (DataMode datamode : dataModes) {

					List<String> regexs = datamode.getUrls().getRegex();

					for (String regex : regexs) {

						Links links = new Links().addByRegex(doc, regex);

						//XXX 没有实现完成

					}

				}

			}

		}

	}


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
		// TODO Auto-generated method stub

	}


	@Override
	public void setTaskPool(TaskPool taskPool) {
		this.taskPool = taskPool;
	}

}
