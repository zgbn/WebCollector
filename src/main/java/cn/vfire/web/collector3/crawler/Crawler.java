package cn.vfire.web.collector3.crawler;

import java.util.List;

import cn.vfire.web.collector3.crawler.event.ControlCrawlerEvent;
import cn.vfire.web.collector3.crawler.event.CrawlerEvent;
import cn.vfire.web.collector3.crawler.event.DefaultCrawlerEvent;
import cn.vfire.web.collector3.crawler.executor.DefaultExecutor;
import cn.vfire.web.collector3.crawler.executor.Executor;
import cn.vfire.web.collector3.crawler.executor.Requester;
import cn.vfire.web.collector3.crawler.pool.FetcherThreadPool;
import cn.vfire.web.collector3.crawler.pool.ProxyFetcherPool;
import cn.vfire.web.collector3.crawler.pool.TaskPool;
import cn.vfire.web.collector3.crawler.snapshot.CrawlSnapshot;
import cn.vfire.web.collector3.crawler.visitor.CrawlerVisitor;
import cn.vfire.web.collector3.crawler.visitor.ProxyCrawlerVisitor;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.Tools;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 爬虫，一个爬虫只执行一个爬虫批任务。
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class Crawler {

	private CrawlerAttrInfo crawlerAttrInfo;

	/** 爬虫任务配置信息对象 */
	private CrawlerConfig config;

	/** 爬虫ID */
	private String id;

	/** 爬虫触手 */
	@Getter
	@Setter
	private Fetcher fetcher;

	/** 注入任务池，任务池中有数据爬虫才会工作。 */
	@Getter
	@Setter
	private TaskPool taskPool;

	/** 爬虫快照 */
	@Getter
	@Setter
	private CrawlSnapshot snapshot;

	/** 爬虫执行者 */
	@Getter
	@Setter
	private Executor executor;

	@Getter
	@Setter
	private Requester requester;

	/** 爬虫事件对象集合 */
	private DefaultCrawlerEvent event;

	/** 爬虫参与者，用于处理爬虫Page个性化处理 */
	private ProxyCrawlerVisitor visitor;

	/** 爬虫触手线程池 */
	private ProxyFetcherPool fetcherPool;


	/**
	 * 创建一个默认的爬虫对象,需要setter注入其他属性
	 * 
	 * @param config
	 *            爬虫配置文件对象
	 */
	public Crawler(CrawlerConfig config) {
		this.init(config, null);
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
		this.init(config, null);
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
		return (long) this.fetcher.getTotalCount();
	}


	private void init(CrawlerConfig config, TaskPool taskPool) {

		this.config = config;

		this.taskPool = taskPool;

		this.id = this.config.getId();

		this.crawlerAttrInfo = new CrawlerAttrInfo().formObj(this.config);

		this.fetcher = new Fetcher(this.id);

		this.snapshot = null;

		this.executor = new DefaultExecutor();

		this.requester = null;

		this.event = new DefaultCrawlerEvent();
		this.event.setName(this.id);
		this.event.addEvent(new ControlCrawlerEvent());

		this.visitor = new ProxyCrawlerVisitor();

		this.fetcherPool = new ProxyFetcherPool();

		log.info("爬虫{}初始化完成。", this.id);

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
	 * 注入爬虫触手线程池
	 * 
	 * @param fetcherPool
	 */
	public void setFetcherPool(FetcherThreadPool fetcherPool) {
		this.fetcherPool.setFetcherThreadPool(fetcherPool);
	}


	/**
	 * 注入参与者
	 * 
	 * @param visitor
	 */
	public void setVisitor(CrawlerVisitor visitor) {
		this.visitor.setCrawlerVisitor(visitor);
	}


	/**
	 * 开始运行爬虫
	 */
	public void start() {

		log.info("爬虫{}开始运行，运行参数{}。", this.id, Tools.toStringByFieldLabel(this.crawlerAttrInfo));

		this.event.crawlerBefore(this.config);

		this.startFetcherPool();

		this.event.crawlerAfer(this.fetcherPool.getRuntime(), this.fetcherPool.getCrawlDatumCount(),
				this.fetcherPool.getActiveThreads());

		log.info("爬虫{}全部任务完成并已停止，总共耗时{}毫秒，总共执行{}次采集，运行时平均并发线程数为{}条。", this.id, this.fetcherPool.getRuntime(),
				this.fetcherPool.getCrawlDatumCount(), this.fetcherPool.getActiveThreads());
	}


	private void startFetcherPool() {

		this.fetcher.setEvent(this.event);
		this.fetcher.setExecutor(this.executor);
		this.fetcher.setSnapshot(this.snapshot);
		this.fetcher.setTaskPool(this.taskPool);
		this.fetcher.setVisitor(this.visitor);

		this.fetcherPool.setInc(this.config.getIncthreads());
		this.fetcherPool.setInitThread(this.config.getThreads());
		this.fetcherPool.setMaxThread(this.config.getMaxthreads());
		this.fetcherPool.setMinThread(this.config.getMinthreads());
		this.fetcherPool.setFetcher(this.fetcher);

		Tools.setWareField(this, this.crawlerAttrInfo, this.taskPool, this.requester);

		this.fetcherPool.execute();

	}

}
