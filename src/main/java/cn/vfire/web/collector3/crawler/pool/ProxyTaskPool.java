package cn.vfire.web.collector3.crawler.pool;

import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.tools.pool.TaskPool;

public class ProxyTaskPool extends TaskPool {

	private TaskPool pool;

	public ProxyTaskPool(String id) {
		super();
		this.pool = new DefaultTaskPool(id);
	}

	public void setPool(TaskPool pool) {
		if (!(pool instanceof Default))
			this.pool = pool;
	}

	@Override
	public boolean isEmpty() {
		return this.pool.isEmpty();
	}

	@Override
	public TaskPool offer(CrawlDatum crawlDatum) {
		return this.pool.offer(crawlDatum);
	}

	@Override
	public CrawlDatum poll() {
		return this.pool.poll();
	}

	@Override
	public void save(CrawlDatum crawlDatum) {
		this.pool.save(crawlDatum);
	}

}
