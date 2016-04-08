package cn.vfire.web.collector2.event;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import cn.vfire.web.collector2.model.CrawlDatum;
import cn.vfire.web.collector2.model.Page;
import cn.vfire.web.collector2.tools.TaskPool;

public class DefaultCrawlerEvent implements CrawlerEvent {

	private List<CrawlerEvent> list = new LinkedList<CrawlerEvent>();

	private boolean isSort = true;


	public void addEvent(CrawlerEvent event) {
		if (event.index() != this.index()) {
			this.isSort = false;
		}
		this.list.add(event);
	}


	public List<CrawlerEvent> sortlist() {

		if (this.isSort == false) {
			Collections.sort(list, new Comparator<CrawlerEvent>() {

				@Override
				public int compare(CrawlerEvent o1, CrawlerEvent o2) {
					return o1.index() - o2.index();
				}
			});
			this.isSort = true;
		}

		return list;
	}


	@Override
	public int index() {
		return 0;
	}


	@Override
	public void crawlerBefore() {
		for (CrawlerEvent event : this.sortlist()) {
			event.crawlerBefore();
		}
	}


	@Override
	public void crawlerAfer() {
		for (CrawlerEvent event : this.sortlist()) {
			event.crawlerAfer();
		}
	}


	@Override
	public void crawlerAferAsyn() {
		for (CrawlerEvent event : this.sortlist()) {
			event.crawlerAferAsyn();
		}
	}


	@Override
	public boolean facherBefore(CrawlDatum crawlDatum, TaskPool taskPool, int count) {
		boolean flag = true;
		for (CrawlerEvent event : this.sortlist()) {
			flag = flag && event.facherBefore(crawlDatum, taskPool, count);
		}
		return flag;
	}


	@Override
	public void facherAferAsyn(final Page page, final TaskPool taskPool, final int count) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (CrawlerEvent event : DefaultCrawlerEvent.this.sortlist()) {
					event.facherAferAsyn(page, taskPool, count);
				}

			}
		}).start();
	}


	@Override
	public boolean facherAfer(Page page, TaskPool taskPool, int count) {
		boolean flag = true;
		for (CrawlerEvent event : this.sortlist()) {
			flag = flag && event.facherAfer(page, taskPool, count);
		}
		return flag;
	}


	@Override
	public boolean facherExceptin(CrawlDatum crawlDatum, TaskPool taskPool, Exception e, int count) {
		boolean flag = true;
		for (CrawlerEvent event : this.sortlist()) {
			flag = flag && event.facherExceptin(crawlDatum, taskPool, e, count);
		}
		return flag;
	}


	@Override
	public boolean facherStart(String name, int serialNumber, TaskPool taskPool) {
		boolean flag = true;
		for (CrawlerEvent event : this.sortlist()) {
			flag = flag && event.facherStart(name, serialNumber, taskPool);
		}
		return flag;
	}


	@Override
	public void facherEnd(String name, int serialNumber, int exceptionCount, TaskPool taskPool) {
		for (CrawlerEvent event : this.sortlist()) {
			event.facherEnd(name, serialNumber, exceptionCount, taskPool);
		}
	}

}
