package cn.vfire.web.collector3.crawler.pool;

import java.util.concurrent.ConcurrentLinkedQueue;

import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.lang.CrawlerRuntimeException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.tools.pool.TaskPool;

public class DefaultTaskPool extends TaskPool implements Default {

	public DefaultTaskPool(String id) {
		synchronized (TaskQueueMap) {
			if (!TaskQueueMap.containsKey(id)) {
				TaskQueueMap.put(id, new ConcurrentLinkedQueue<CrawlDatum>());
			}
		}
		this.taskQueue = TaskQueueMap.get(id);
	}

	@Override
	public void save(CrawlDatum crawlDatum) {

	}

}
