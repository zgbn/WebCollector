package cn.vfire.web.collector2.model;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 爬虫运行快照
 * 
 * @author ChenGang
 *
 */
public class CrawlSnapshot implements Comparable<CrawlSnapshot> {

	/** 响应时间毫秒 */
	@Getter
	@Setter
	private long time;

	/** 异常计数 */
	private AtomicInteger exceptionCount = new AtomicInteger(0);

	/** 执行任务计数 */
	private AtomicInteger count = new AtomicInteger(0);

	/** 执行个数 */
	private int size;

	/** 爬虫ID */
	@Getter
	@Setter
	private String crawlerId;

	public CrawlSnapshot(String crawlerId, int size) {
		this.crawlerId = crawlerId;
		this.size = size;
	}

	@Override
	public int compareTo(CrawlSnapshot o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
