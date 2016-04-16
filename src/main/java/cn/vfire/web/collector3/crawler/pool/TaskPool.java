package cn.vfire.web.collector3.crawler.pool;

import java.util.concurrent.ConcurrentLinkedQueue;

import cn.vfire.web.collector3.db.DBManager;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;
import lombok.Getter;
import lombok.Setter;

/**
 * 任务池，用于向数据抓取者提供抓取任务，以及存储新的抓取任务。
 * 
 * @author ChenGang
 *
 */
public abstract class TaskPool implements Generator {

	protected ConcurrentLinkedQueue<CrawlDatum> taskQueue = new ConcurrentLinkedQueue<CrawlDatum>();

	@Setter
	private int size = 2000;

	@Getter
	@Setter
	private DBManager dao;


	/** 获取一个分发者对象 */
	public abstract Generator getGenerator();


	public TaskPool(int size) {
		this.size = size;
	}


	protected boolean isEmpty() {
		return this.taskQueue.isEmpty();
	}


	/**
	 * 在当前队列尾部追加一个抓取任务。
	 * 
	 * @param crawlDatum
	 */
	protected TaskPool offer(CrawlDatum crawlDatum) {
		this.taskQueue.offer(crawlDatum);
		return this;
	}


	/**
	 * 获取一个抓取任务数据
	 * 
	 * @return
	 * @throws CrawlerDBException
	 * @throws Exception
	 */
	protected CrawlDatum poll() {
		return this.taskQueue.poll();
	}

}
