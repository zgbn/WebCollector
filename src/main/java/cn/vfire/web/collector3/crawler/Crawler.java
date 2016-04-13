package cn.vfire.web.collector3.crawler;

import java.util.List;

import lombok.Getter;
import cn.vfire.web.collector3.crawler.event.ControlCrawlerEvent;
import cn.vfire.web.collector3.crawler.event.DefaultCrawlerEvent;
import cn.vfire.web.collector3.crawler.executor.DefaultExecutor;
import cn.vfire.web.collector3.crawler.pool.DefaultFetcherPool;
import cn.vfire.web.collector3.crawler.pool.ProxyTaskPool;
import cn.vfire.web.collector3.crawler.snapshot.DefaultCrawlSnapshot;
import cn.vfire.web.collector3.crawler.visitor.ProxyCrawlerVisitor;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector3.tools.crawler.event.CrawlerEvent;
import cn.vfire.web.collector3.tools.crawler.snapshot.CrawlSnapshot;
import cn.vfire.web.collector3.tools.pool.FetcherThreadPool;
import cn.vfire.web.collector3.tools.pool.TaskPool;

/**
 * 爬虫，一个爬虫只执行一个爬虫批任务。
 * 
 * @author ChenGang
 *
 */
public class Crawler {

	/** 爬虫任务配置信息对象 */
	@Getter
	private CrawlerConfig config;

	/** 爬虫ID */
	@Getter
	private String id;

	/** 爬虫触手 */
	@Getter
	private Fetcher fetcher;

	/** 注入任务池，任务池中有数据爬虫才会工作。 */
	@Getter
	private ProxyTaskPool taskPool;

	/** 爬虫快照 */
	@Getter
	private CrawlSnapshot snapshot;

	/** 爬虫事件对象集合 */
	@Getter
	private DefaultCrawlerEvent event;

	/** 爬虫执行者 */
	@Getter
	private DefaultExecutor executor;

	/** 爬虫参与者，用于处理爬虫Page个性化处理 */
	@Getter
	private ProxyCrawlerVisitor visitor;

	/** 爬虫触手线程池 */
	@Getter
	private FetcherThreadPool fetcherPool;

	private void init(CrawlerConfig config) {
		this.config = config;
		this.id = this.config.getId();
		this.fetcher = new Fetcher(this.id);
		this.taskPool = new ProxyTaskPool(this.id);
		this.event = new DefaultCrawlerEvent();
		this.executor = new DefaultExecutor();
		this.visitor = new ProxyCrawlerVisitor();
		this.fetcherPool = new DefaultFetcherPool();
		this.snapshot = new DefaultCrawlSnapshot();
	}

	/**
	 * 创建一个默认的爬虫对象,需要setter注入其他属性
	 * 
	 * @param config
	 *            爬虫配置文件对象
	 */
	public Crawler(CrawlerConfig config) {
		this.init(config);
	}

	/**
	 * 创建一个默认的爬虫对象,需要setter注入其他属性
	 * 
	 * @param config
	 *            爬虫配置文件对象
	 * @param taskPool
	 *            爬虫任务池
	 */
	public Crawler(CrawlerConfig config, TaskPool taskPool) {
		this.init(config);
		this.taskPool.setPool(taskPool);
	}

	/**
	 * 爬虫整体任务完成时间毫秒
	 * 
	 * @return
	 */
	public long getRuntime() {
		return this.fetcherPool.getRuntime();
	}

	/**
	 * 爬虫整体任务完成，统计的总共爬取次数总量。
	 * 
	 * @return
	 */
	public long getTotalCount() {
		return (long) this.fetcher.getCrawlDatumCount();
	}

	/**
	 * 注入爬虫配置信息对象
	 * 
	 * @param config
	 */
	public void setConfig(CrawlerConfig config) {
		this.config = config;
		this.id = config.getId();
	}

	/**
	 * 注入爬虫事件
	 * 
	 * @param crawlerEvent
	 */
	public void setEvent(CrawlerEvent crawlerEvent) {
		this.event.addEvent(crawlerEvent);
	}

	/**
	 * 注入爬虫事件集合
	 * 
	 * @param list
	 */
	public void setEvent(List<CrawlerEvent> list) {
		this.event.addEvent(list);
	}

	/**
	 * 注入自定义爬虫触手线程池。
	 * 
	 * @param fetcherPool
	 */
	public void setFetcherPool(FetcherThreadPool fetcherPool) {
		this.fetcherPool = fetcherPool;
	}

	/**
	 * 注入爬虫运行时快照对象
	 * 
	 * @param snapshot
	 */
	public void setSnapshot(CrawlSnapshot snapshot) {
		this.snapshot = snapshot;
	}

	/**
	 * 注入任务池，必要。
	 * 
	 * @param taskPool
	 */
	public void setTaskPool(TaskPool taskPool) {
		this.taskPool.setPool(taskPool);
	}

	/**
	 * 注入一个自定义的参与者。
	 * 
	 * @param visitor
	 */
	public void setVisitor(CrawlerVisitor visitor) {
		this.visitor.setVisitor(visitor);
	}

	/**
	 * 开始运行爬虫
	 */
	public void start() {
		this.startFetcherPool();
	}

	private void startFetcherPool() {

		// 事件对象注入爬虫配置属性信息
		this.event.setCrawlerAttrInfo(this.config);
		this.event.addEvent(new ControlCrawlerEvent());

		this.fetcher.setEvent(this.event);
		this.fetcher.setExecutor(this.executor);
		this.fetcher.setTaskPool(this.taskPool);
		this.fetcher.setSnapshot(this.snapshot);

		this.fetcherPool.setFetcher(this.fetcher);
		this.fetcherPool.setInc(this.config.getIncthreads());
		this.fetcherPool.setInitThread(this.config.getThreads());
		this.fetcherPool.setMaxThread(this.config.getMaxthreads());
		this.fetcherPool.setMinThread(this.config.getMinthreads());

		this.event.crawlerBefore(this.config);

		this.fetcherPool.execute();

		this.event.crawlerAfer(this.fetcherPool.getRuntime(), this.fetcherPool.getCrawlDatumCount(), this.fetcherPool.getActiveThreads());

	}

}
