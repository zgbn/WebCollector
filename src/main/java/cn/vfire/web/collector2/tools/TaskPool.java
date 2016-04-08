package cn.vfire.web.collector2.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.vfire.web.collector2.lang.TaskStorageException;
import cn.vfire.web.collector2.model.CrawlDatum;

/**
 * 任务池，用于向数据抓取者提供抓取任务，以及存储新的抓取任务。
 * 
 * @author ChenGang
 *
 */
public abstract class TaskPool {

	private static Map<String, ConcurrentLinkedQueue<CrawlDatum>> TaskQueueMap = new HashMap<String, ConcurrentLinkedQueue<CrawlDatum>>();

	private final ConcurrentLinkedQueue<CrawlDatum> taskQueue;


	public TaskPool(String id) {
		synchronized (TaskQueueMap) {
			if (!TaskQueueMap.containsKey(id)) {
				TaskQueueMap.put(id, new ConcurrentLinkedQueue<CrawlDatum>());
			}
		}
		this.taskQueue = TaskQueueMap.get(id);
	}


	/**
	 * 在当前队列尾部追加一个抓取任务。
	 * 
	 * @param crawlDatum
	 */
	public TaskPool add(CrawlDatum crawlDatum) {
		this.taskQueue.offer(crawlDatum);
		return this;
	}


	/**
	 * 获取一个抓取任务数据
	 * 
	 * @return
	 * @throws TaskStorageException
	 * @throws Exception
	 */
	public CrawlDatum get() {
		return this.taskQueue.poll();
	}

}
