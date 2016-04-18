package cn.vfire.web.collector3.crawler.defaults;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import lombok.extern.slf4j.Slf4j;

import org.jsoup.nodes.Document;

import cn.vfire.web.collector3.crawler.visitor.CrawlerVisitor;
import cn.vfire.web.collector3.crawler.ware.AbsSuperWare;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.crawler.ware.RequesterWare;
import cn.vfire.web.collector3.lang.FatchStopException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Links;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.RegexRule;
import cn.vfire.web.collector3.model.ResultData;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;

/**
 * 爬虫默认参与者。 支持多个参与者注入，其他参与者如果抛出异常则做无效处理。
 * 
 * @author ChenGang
 *
 */
@Slf4j
public final class DefaultCrawlerVisitor extends AbsSuperWare implements CrawlerVisitor, Default {

	/** 爬虫事件集合 */
	private Set<CrawlerVisitor> list = new TreeSet<CrawlerVisitor>(new Comparator<CrawlerVisitor>() {

		@Override
		public int compare(CrawlerVisitor o1, CrawlerVisitor o2) {
			return o1.index() - o2.index();
		}
	});

	private Map<Integer, Integer> topNMap = new TreeMap<Integer, Integer>();

	/**
	 * 参与在爬虫爬取之前对爬取任务处理操作。
	 * 
	 * @param crawlDatum
	 *            从库中获取的新的爬取任务。
	 * @return 如果返回false，爬虫则跳过该任务，执行下一个爬取任务。
	 * @throws FatchStopException
	 *             当抛出此异常，则爬虫直接退出。
	 */
	public boolean fetchCrawlDatum(CrawlDatum crawlDatum) throws FatchStopException {

		int depth = crawlDatum.getDepth();

		int execount = crawlDatum.getExeCount();

		int expcount = crawlDatum.getExpCount();

		if (this.crawlerAttrInfo.isProxy()) {
			crawlDatum.setProxy(this.proxyIpPool.getProxy());
		}

		if (depth > this.crawlerAttrInfo.getDepth()) {
			crawlDatum.setInvalid(true);
			log.debug("该CrawlDatum任务数据达到限定的爬取深度{}，跳过该任务。", depth);
			return false;
		}

		if (execount > this.crawlerAttrInfo.getMaxExecuteCount()) {
			crawlDatum.setInvalid(true);
			log.debug("该CrawlDatum任务数据达到限定的爬取次数{}，跳过该任务。", execount);
			return false;
		}

		if (expcount > this.crawlerAttrInfo.getRetry()) {
			crawlDatum.setInvalid(true);
			log.debug("该CrawlDatum任务数据达到限定的爬取异常处理次数{}，跳过该任务。", expcount);
			return false;
		}

		if (this.crawlerAttrInfo.getTopum() > 0) {
			synchronized (topNMap) {
				Integer cc = topNMap.get(depth);
				cc = cc == null ? 1 : cc + 1;
				if (cc > this.crawlerAttrInfo.getTopum()) {
					log.debug("该CrawlDatum任务数据在到达当前深度{}限定的任务个数{}，跳过该任务。", depth, this.crawlerAttrInfo.getTopum());
					return false;
				}
				topNMap.put(depth, cc);
			}
		}

		return true;
	}

	/**
	 * 从Page中提取新的任务
	 * 
	 * @param page
	 * @return
	 */
	public Links fetchCrawlDatum(Page page) {

		Links linksAll = new Links();

		String conteType = page.getResponse().getContentType();

		if (conteType != null && conteType.contains("text/html")) {

			Document doc = page.doc();

			if (doc != null) {

				RegexRule regexRule = new RegexRule(this.regexRules, this.unregexRules);

				Links links = new Links().addByRegex(doc, regexRule, page.getCrawlDatum().getDepth() + 1);

				linksAll.add(links);

			}
		}

		return linksAll;

	}

	/**
	 * 参与从Page中提取新的爬取link任务操作。
	 * 
	 * @param page
	 *            爬虫抓取到的结果Page
	 * @param links
	 *            从page中提取出来的新的爬取任务。
	 * @see cn.vfire.web.collector3.crawler.visitor.CrawlerVisitor#fetchCrawlDatum
	 *      (cn.vfire.web.collector3.model.Page,
	 *      cn.vfire.web.collector3.model.Links)
	 */
	@Override
	public Links fetchParseLinks(Page page, Links links) {

		Links rlists = this.fetchCrawlDatum(page);

		Links tmplist = new Links();

		for (CrawlerVisitor visitor : this.list) {

			try {

				Links incRLists = visitor.fetchParseLinks(page, links);

				if (incRLists != null) {
					tmplist.add(incRLists);
				}

			} catch (Throwable e) {
				log.warn("参与者{}-{}执行发生异常，该异常被忽略。", visitor.index(), visitor.getClass().getName(), e);
			}
		}

		rlists.add(tmplist);

		return rlists;

	}

	/**
	 * 提取当前页面中数据
	 * 
	 * @param page
	 *            爬虫抓取到的结果Page
	 * @return
	 */
	public List<ResultData> fetchResultData(Page page, DataMode datamode) {

		List<ResultData> resultDatas = new LinkedList<ResultData>();

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

			}
		}

		return resultDatas;
	}

	/**
	 * 参与对爬虫抓取到的Page信息和提取结果数据操作。<br />
	 * 
	 * @param page
	 *            爬虫抓取到的结果Page
	 * @param resultData
	 *            爬虫根据配置中datamode描述从page对象中提取到的结果数据，以datamode为单位。
	 * @see cn.vfire.web.collector3.crawler.visitor.CrawlerVisitor#fetchResultData
	 *      (cn.vfire.web.collector3.model.Page, java.util.List)
	 */
	@Override
	public List<ResultData> fetchResultData(Page page, List<ResultData> resultData) {

		List<ResultData> resultlist = new LinkedList<ResultData>();

		if (this.dataModes != null) {

			for (DataMode datamode : this.dataModes) {

				List<ResultData> tmplist = new LinkedList<ResultData>();

				List<ResultData> rdata = this.fetchResultData(page, datamode);

				for (CrawlerVisitor visitor : this.list) {
					try {
						List<ResultData> incRData = visitor.fetchResultData(page, rdata);
						if (incRData != null) {
							tmplist.addAll(incRData);
						}
					} catch (Throwable e) {
						log.warn("参与者{}-{}执行发生异常，该异常被忽略。", visitor.index(), visitor.getClass().getName(), e);
					}
				}

				rdata.addAll(tmplist);

				try {
					datamode.getOutdata().getFormatclass().getFormatData().setResultData(rdata);
				} catch (Exception e) {
					e.printStackTrace();
				}

				resultlist.addAll(rdata);

			}
		} else {
			log.error("爬虫提取数据的映射模型对象{}为{}。", DataMode.class, this.dataModes);
		}

		log.debug("采集数据中提取的新任务数据:{}", resultlist);

		return resultlist;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.vfire.web.collector3.crawler.visitor.CrawlerVisitor#index()
	 */
	@Override
	public int index() {
		return 0;
	}

	/**
	 * 注入新的观察者
	 * 
	 * @param visitor
	 */
	public void setVisitor(CrawlerVisitor visitor) {
		if (visitor instanceof Default) {
			return;
		}
		if (visitor instanceof CrawlerInfoWare) {
			((CrawlerInfoWare) visitor).setCrawlerAttrInfo(this.crawlerAttrInfo);
		}
		if (visitor instanceof RequesterWare) {
			((RequesterWare) visitor).setRequester(this.requester);
		}
		this.list.add(visitor);
	}

}
