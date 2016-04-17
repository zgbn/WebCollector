package cn.vfire.web.collector3.crawler.event;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.crawler.executor.Requester;
import cn.vfire.web.collector3.crawler.pool.TaskPool;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.crawler.ware.RequesterWare;
import cn.vfire.web.collector3.crawler.ware.TaskPoolWare;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.tools.crawler.element.ProxyIP;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听事件的默认实现。 <br />
 * XXX 需要进一步优化
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class DefaultCrawlerEvent implements CrawlerEvent, CrawlerInfoWare, TaskPoolWare, RequesterWare, Default {

	/** 爬虫事件集合 */
	private Set<CrawlerEvent> list = new TreeSet<CrawlerEvent>(new Comparator<CrawlerEvent>() {

		@Override
		public int compare(CrawlerEvent o1, CrawlerEvent o2) {
			return o1.index() - o2.index();
		}
	});

	/** 爬虫属性信息 */
	private CrawlerAttrInfo crawlerAttrInfo;

	/** 爬虫名字 */
	private String name;

	@SuppressWarnings("unused")
	private Requester requester;

	@SuppressWarnings("unused")
	private TaskPool taskPool;

	@SuppressWarnings("unused")
	private ProxyIP proxyIPs;


	public void addEvent(CrawlerEvent event) {
		this.list.add(event);
		log.debug("为Crawler爬虫添加事件对象。{}", event);
	}


	public void addEvent(List<CrawlerEvent> list) {
		this.list.addAll(list);
		log.debug("为Crawler爬虫添加事件对象。{}", list);
	}


	@Override
	public void crawlerAfer(long runtime, int crawlDatumCount, int activeThreads) {
		log.debug("爬虫Crawler:{}的crawlerAfer事件触发。", name);
		for (CrawlerEvent event : this.list) {
			this.setCrawlerAttrInfo(event);
			event.crawlerAfer(runtime, crawlDatumCount, activeThreads);
		}
	}


	@Override
	public void crawlerBefore() {
		log.debug("爬虫Crawler:{}的crawlerBefore事件触发。", name);
		for (CrawlerEvent event : this.list) {
			this.setCrawlerAttrInfo(event);
			event.crawlerBefore();
		}
	}


	@Override
	public void facherAfer(Page page, TaskPool taskPool) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherAfer事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			this.setCrawlerAttrInfo(event);
			event.facherAfer(page, taskPool);
		}
	}


	@Override
	public void facherBefore(CrawlDatum crawlDatum, TaskPool taskPool) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherBefore事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			this.setCrawlerAttrInfo(event);
			event.facherBefore(crawlDatum, taskPool);
		}
	}


	@Override
	public void facherEnd(int serialNumber, int exeCount, TaskPool taskPool) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherEnd事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			this.setCrawlerAttrInfo(event);
			event.facherEnd(serialNumber, exeCount, taskPool);
		}
	}


	@Override
	public void facherExceptin(CrawlDatum crawlDatum, TaskPool taskPool, Exception e) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherExceptin事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			this.setCrawlerAttrInfo(event);
			event.facherExceptin(crawlDatum, taskPool, e);
		}
	}


	@Override
	public void facherStart(int serialNumber, TaskPool taskPool) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherStart事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			this.setCrawlerAttrInfo(event);
			event.facherStart(serialNumber, taskPool);
		}
	}


	@Override
	public int index() {
		return 0;
	}


	@Override
	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo) {
		this.crawlerAttrInfo = crawlerAttrInfo;
	}


	public void setCrawlerAttrInfo(CrawlerEvent event) {
		if (event instanceof CrawlerInfoWare) {
			CrawlerInfoWare _event = (CrawlerInfoWare) event;
			_event.setCrawlerAttrInfo(this.crawlerAttrInfo);
			_event.setName(this.name);
		}
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


	@Override
	public void setTaskPool(TaskPool taskPool) {
		this.taskPool = taskPool;

	}
}
