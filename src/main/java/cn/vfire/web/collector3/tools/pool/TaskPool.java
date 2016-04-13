package cn.vfire.web.collector3.tools.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.Setter;
import cn.vfire.web.collector3.lang.CrawlerRuntimeException;
import cn.vfire.web.collector3.lang.TaskStorageException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;

/**
 * 任务池，用于向数据抓取者提供抓取任务，以及存储新的抓取任务。
 * 
 * @author ChenGang
 *
 */
public abstract class TaskPool {

	private static Map<String, ConcurrentLinkedQueue<CrawlDatum>> TaskQueueMap = new HashMap<String, ConcurrentLinkedQueue<CrawlDatum>>();

	protected ConcurrentLinkedQueue<CrawlDatum> taskQueue;

	@Setter
	private int size = 2000;

	protected TaskPool() {
	}

	public TaskPool(String id) {
		synchronized (TaskQueueMap) {
			if (!TaskQueueMap.containsKey(id)) {
				TaskQueueMap.put(id, new ConcurrentLinkedQueue<CrawlDatum>());
			} else {
				throw new CrawlerRuntimeException(CrawlerExpInfo.EXIST.setInfo("创建TaskPool实例失败，原因是在TaskPool中已经存在ID={}的TaskPool实例。", id));
			}
		}
		this.taskQueue = TaskQueueMap.get(id);
	}

	public boolean isEmpty() {
		return this.taskQueue.isEmpty();
	}

	/**
	 * 在当前队列尾部追加一个抓取任务。
	 * 
	 * @param crawlDatum
	 */
	public TaskPool offer(CrawlDatum crawlDatum) {
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
	public CrawlDatum poll() {
		return this.taskQueue.poll();
	}

	/**
	 * 保存新的任务持久化
	 * 
	 * @param crawlDatum
	 */
	public abstract void save(CrawlDatum crawlDatum);

}
