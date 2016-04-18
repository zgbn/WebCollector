package cn.vfire.web.collector3.crawler.defaults;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import cn.vfire.web.collector3.crawler.event.CrawlerEvent;
import cn.vfire.web.collector3.crawler.ware.AbsSuperWare;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.crawler.ware.RequesterWare;
import cn.vfire.web.collector3.lang.FatchStopException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.model.Page;

/**
 * 监听事件的默认实现。 <br />
 * 
 * @author ChenGang
 *
 */
public final class DefaultCrawlerEvent extends AbsSuperWare implements CrawlerEvent, Default {

	/** 爬虫事件集合 */
	private Set<CrawlerEvent> list = new TreeSet<CrawlerEvent>(new Comparator<CrawlerEvent>() {

		@Override
		public int compare(CrawlerEvent o1, CrawlerEvent o2) {
			return o1.index() - o2.index();
		}
	});

	@Override
	public void crawlerAfer(long runtime, int crawlDatumCount, int activeThreads) {
		for (CrawlerEvent event : this.list) {
			event.crawlerAfer(runtime, crawlDatumCount, activeThreads);
		}
	}

	@Override
	public void crawlerBefore() {
		for (CrawlerEvent event : this.list) {
			event.crawlerBefore();
		}
	}

	@Override
	public void facherAfer(Page page) throws FatchStopException {
		for (CrawlerEvent event : this.list) {
			event.facherAfer(page);
		}
	}

	@Override
	public void facherBefore(CrawlDatum crawlDatum) throws FatchStopException {
		for (CrawlerEvent event : this.list) {
			event.facherBefore(crawlDatum);
		}
	}

	@Override
	public void facherEnd() throws FatchStopException {
		for (CrawlerEvent event : this.list) {
			event.facherEnd();
		}
	}

	@Override
	public void facherExceptin(CrawlDatum crawlDatum, Exception e) throws FatchStopException {
		for (CrawlerEvent event : this.list) {
			event.facherExceptin(crawlDatum, e);
		}
	}

	@Override
	public void facherStart() throws FatchStopException {
		for (CrawlerEvent event : this.list) {
			event.facherStart();
		}
	}

	@Override
	public int index() {
		return 0;
	}

	/**
	 * 注入爬虫基本属性
	 */
	@Override
	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo) {
		this.crawlerAttrInfo = crawlerAttrInfo;
	}

	/**
	 * 注入事件
	 * 
	 * @param event
	 */
	public void setEvent(CrawlerEvent event) {
		if (event instanceof Default) {
			return;
		}
		if (event instanceof CrawlerInfoWare) {
			((CrawlerInfoWare) event).setCrawlerAttrInfo(this.crawlerAttrInfo);
		}
		if (event instanceof RequesterWare) {
			((RequesterWare) event).setRequester(this.requester);
		}
		this.list.add(event);
	}

}
