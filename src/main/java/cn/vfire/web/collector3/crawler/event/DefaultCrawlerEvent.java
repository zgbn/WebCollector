package cn.vfire.web.collector3.crawler.event;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector3.crawler.CrawlerInfo;
import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector3.tools.crawler.event.CrawlerEvent;
import cn.vfire.web.collector3.tools.pool.TaskPool;

@Slf4j
public class DefaultCrawlerEvent implements CrawlerEvent, CrawlerInfo, Default {

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

	public void addEvent(CrawlerEvent event) {
		this.list.add(event);
		log.debug("为Crawler爬虫添加事件对象。{}", event);
	}

	public void addEvent(List<CrawlerEvent> list) {
		this.list.addAll(list);
		log.debug("为Crawler爬虫添加事件对象。{}", list);
	}

	@Override
	public int index() {
		return 0;
	}

	@Override
	public void crawlerBefore(CrawlerConfig config) {
		log.debug("爬虫Crawler:{}的crawlerBefore事件触发。", name);
		for (CrawlerEvent event : this.list) {
			event.crawlerBefore(config);
		}
	}

	@Override
	public void crawlerAfer(long runtime, int crawlDatumCount, int activeThreads) {
		log.debug("爬虫Crawler:{}的crawlerAfer事件触发。", name);
		for (CrawlerEvent event : this.list) {
			event.crawlerAfer(runtime, crawlDatumCount, activeThreads);
		}
	}

	@Override
	public void facherBefore(CrawlDatum crawlDatum, TaskPool taskPool) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherBefore事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			event.facherBefore(crawlDatum, taskPool);
		}
	}

	@Override
	public void facherAfer(Page page, TaskPool taskPool) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherAfer事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			event.facherAfer(page, taskPool);
		}
	}

	@Override
	public void facherExceptin(CrawlDatum crawlDatum, TaskPool taskPool, Exception e) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherExceptin事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			event.facherExceptin(crawlDatum, taskPool, e);
		}
	}

	@Override
	public void facherStart(int serialNumber, TaskPool taskPool) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherStart事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			event.facherStart(serialNumber, taskPool);
		}
	}

	@Override
	public void facherEnd(int serialNumber, int exeCount, TaskPool taskPool) {
		log.debug("爬虫Crawler:{}触手Facher:{}的facherEnd事件触发。", name, Thread.currentThread().getName());
		for (CrawlerEvent event : this.list) {
			event.facherEnd(serialNumber, exeCount, taskPool);
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
		for (CrawlerEvent event : this.list) {
			event.setName(name);
		}
	}

	@Override
	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo) {
		this.crawlerAttrInfo = crawlerAttrInfo;
		for (CrawlerEvent event : this.list) {
			event.setCrawlerAttrInfo(this.crawlerAttrInfo);
		}
	}

	@Override
	public void setCrawlerAttrInfo(CrawlerConfig config) {
		this.crawlerAttrInfo = new CrawlerAttrInfo().formObj(config);
		for (CrawlerEvent event : this.list) {
			event.setCrawlerAttrInfo(this.crawlerAttrInfo);
		}
	}
}
