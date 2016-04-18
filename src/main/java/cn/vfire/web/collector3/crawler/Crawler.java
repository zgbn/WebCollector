package cn.vfire.web.collector3.crawler;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.crawler.defaults.DefaultCrawlerEvent;
import cn.vfire.web.collector3.crawler.defaults.DefaultCrawlerVisitor;
import cn.vfire.web.collector3.crawler.defaults.DefaultExecutor;
import cn.vfire.web.collector3.crawler.event.CrawlerEvent;
import cn.vfire.web.collector3.crawler.executor.Executor;
import cn.vfire.web.collector3.crawler.executor.Requester;
import cn.vfire.web.collector3.crawler.pool.FetcherThreadPool;
import cn.vfire.web.collector3.crawler.snapshot.CrawlSnapshot;
import cn.vfire.web.collector3.crawler.visitor.CrawlerVisitor;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.Tools;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;

/**
 * 爬虫，一个爬虫只执行一个爬虫批任务。
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class Crawler {

	// private CrawlerAttrInfo crawlerAttrInfo;

	/** 爬虫任务配置信息对象 */
	private CrawlerConfig config;

	/** 爬虫ID */
	@Getter
	private String id;

	/** 爬虫的名字 */
	@Getter
	private String name;

	/** 爬虫触手 */
	@Getter
	private Fetcher fetcher;

	@Setter
	private DBManager dbManager;

	/** 爬虫快照 */
	@Getter
	@Setter
	private CrawlSnapshot snapshot;

	/** 爬虫执行者 */
	@Getter
	private Executor executor;

	/** 可恢复设置，true是继续上次未完成的任务执行，false则重新开始执行。 */
	@Getter
	@Setter
	private boolean resumable;

	@Getter
	@Setter
	private Requester requester;

	/** 爬虫事件对象集合 */
	private DefaultCrawlerEvent event;

	/** 爬虫参与者，用于处理爬虫Page个性化处理 */
	private DefaultCrawlerVisitor visitor;

	/** 爬虫触手线程池 */
	@Setter
	private FetcherThreadPool fetcherPool;

	/** 爬虫的描述 */
	@Getter
	private String description;

	@Getter
	private String seedurl;

	@Getter
	private CrawlerAttrInfo crawlerAttrInfo;

	/**
	 * 创建一个默认的爬虫对象,需要setter注入其他属性
	 * 
	 * @param config
	 *            爬虫配置文件对象
	 */
	public Crawler(CrawlerConfig config) {
		this.config = config;
	}

	/**
	 * 爬虫整体任务完成时间毫秒
	 * 
	 * @return
	 */
	public long getRuntime() {
		return this.fetcherPool.getRuntime();
	}

	public int getTotalCount() {
		return this.fetcherPool.getTotalCount();
	}

	private void init(CrawlerConfig config) throws CrawlerDBException {

		this.config = config;

		this.id = this.config.getId();

		this.name = this.config.getName();

		this.description = this.config.getDescription();

		this.seedurl = this.config.getSeedurl();

		this.crawlerAttrInfo = new CrawlerAttrInfo().formObj(this.config);

		this.fetcher = new Fetcher(this.id);

		this.executor = new DefaultExecutor();

		this.visitor = new DefaultCrawlerVisitor();

		this.event = new DefaultCrawlerEvent();

		if (this.requester == null) {
			if (this.executor instanceof DefaultExecutor) {
				this.requester = (DefaultExecutor) this.executor;
			}
		}

		/* 初始化任务数据库 */
		if (this.resumable == false) {
			this.dbManager.clear();
			this.dbManager.inject(new CrawlDatum(this.seedurl, 1));
		}

		log.info("爬虫{}初始化完成。", this.id);

	}

	/**
	 * 注入爬虫事件
	 * 
	 * @param crawlerEvent
	 */
	public void setEvent(CrawlerEvent crawlerEvent) {
		this.event.setEvent(crawlerEvent);
	}

	/**
	 * 注入爬虫事件集合
	 * 
	 * @param list
	 */
	public void setEvent(List<CrawlerEvent> list) {
		for (CrawlerEvent e : list) {
			this.event.setEvent(e);
		}
	}

	public void setVisitor(CrawlerVisitor visitor) {
		this.visitor.setVisitor(visitor);
	}

	/**
	 * 开始运行爬虫
	 * 
	 * @throws CrawlerDBException
	 */
	public void start() throws CrawlerDBException {

		this.init(config);

		log.info("爬虫{}初始化。", this.id);

		this.event.crawlerBefore();

		log.info("爬虫{}开始运行。", this.id);

		this.startFetcherPool();

		this.event.crawlerAfer(this.fetcherPool.getRuntime(), this.getTotalCount(), this.fetcherPool.getActiveThreads());

		log.info("爬虫{}全部任务完成并已停止，总共耗时{}毫秒，总共执行{}次采集，运行时平均并发线程数为{}条。", this.id, this.fetcherPool.getRuntime(), this.fetcherPool.getTotalCount(),
				this.fetcherPool.getActiveThreads());
	}

	private void startFetcherPool() {

		this.fetcher.setEvent(this.event);
		this.fetcher.setExecutor(this.executor);
		this.fetcher.setSnapshot(this.snapshot);
		this.fetcher.setVisitor(this.visitor);
		this.fetcher.setDbManager(this.dbManager);

		this.fetcherPool.setFetcher(this.fetcher);

		Tools.setWareField(this, this.config, this.requester);

		this.fetcherPool.execute();

	}

}
