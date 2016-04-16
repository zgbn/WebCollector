package cn.vfire.web.collector3.crawler.event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector3.crawler.pool.TaskPool;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.tools.Tools;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;

/**
 * XXX 需要实现接口方法
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class ControlCrawlerEvent implements CrawlerEvent {

	private CrawlerAttrInfo crawlerAttrInfo;

	@Getter
	private String name;


	@Override
	public void crawlerAfer(long runtime, int crawlDatumCount, int activeThreads) {
		log.info("爬虫{}全部任务结束，总耗时{}ms，总抓取{}次URL，并发触手个数{}个。", runtime, crawlDatumCount, activeThreads);
	}


	@Override
	public void crawlerBefore(CrawlerConfig config) {
		log.info("爬虫{}加载任务{}完成，并开始执行。任务描述信息为{}。", config.getId(), config.getName(),
				Tools.toStringByFieldLabel(this.crawlerAttrInfo));
	}


	@Override
	public void facherAfer(Page page, TaskPool taskPool) {
	}


	@Override
	public void facherBefore(CrawlDatum crawlDatum, TaskPool taskPool) {
		if (crawlDatum != null) {
			int maxCount = this.crawlerAttrInfo.getMaxExecuteCount();
			int exeCount = crawlDatum.getExeCount();
			if (exeCount > maxCount) {
				crawlDatum.setInvalid(true);
				log.debug("此任务{}已经重复执行{}次，删除该任务，爬虫定义每个任务最多执行{}次。", exeCount, maxCount);
			}
		}
	}


	@Override
	public void facherEnd(int serialNumber, int exeCount, TaskPool taskPool) {
	}


	@Override
	public void facherStart(int serialNumber, TaskPool taskPool) {
	}


	@Override
	public int index() {
		return Integer.MIN_VALUE;
	}


	@Override
	public void facherExceptin(CrawlDatum crawlDatum, TaskPool taskPool, Exception e) {
		// TODO Auto-generated method stub
	}

}
