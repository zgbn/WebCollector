package cn.vfire.web.collector3.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public class CrawlDatums implements Iterable<CrawlDatum> {

	@Getter
	private List<CrawlDatum> dataList = new LinkedList<CrawlDatum>();

	@Override
	public Iterator<CrawlDatum> iterator() {
		return dataList.iterator();
	}

	public CrawlDatums(CrawlDatums crawlDatums) {
		this.addAll(crawlDatums.dataList);
	}

	public boolean addAll(Collection<CrawlDatum> crawlDatum) {
		return this.dataList.addAll(crawlDatum);
	}

}
