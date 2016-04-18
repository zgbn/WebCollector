package cn.vfire.web.collector3.model;

import lombok.Getter;
import lombok.Setter;
import cn.vfire.web.collector3.annotation.Label;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;

public class CrawlerAttrInfo {

	/** 爬虫ID */
	@Getter
	@Setter
	@Label("爬虫ID")
	private String id;

	/** 爬虫中文名称 */
	@Getter
	@Setter
	@Label("爬虫名称")
	private String name;

	/** 爬虫描述文字 */
	@Getter
	@Setter
	@Label("爬虫描述")
	private String description;

	/** 爬虫的种子 */
	@Getter
	@Setter
	@Label("种子URL")
	private String seedurl;

	/** 爬虫爬取页面的深度，默认为1，即只爬去种子页面。 */
	@Getter
	@Setter
	@Label("页面深度")
	private int depth = 1;

	/** 爬虫是否使用代理服务器IP */
	@Getter
	@Setter
	@Label("是否使用代理请求")
	private boolean isProxy = false;

	/**
	 * 规定的当爬虫访问URL失败时候，爬虫会将该URL放回抓去URL任务池中的次数，也就规定爬虫对该URL重复抓取 次数，非当前线程。
	 */
	@Getter
	@Setter
	@Label("页面最大重复执行次数")
	private int maxExecuteCount = 5;

	/** 爬虫执行时的最大线程数，当给定默认线程数不是最优的，爬虫测试时最大线程数。 */
	@Getter
	@Setter
	@Label("最大并发线程数")
	private int maxhreads = 50;

	/**
	 * 爬虫执行时的最小线程数，当给定默认线程数不是最优的，爬虫测试后的最佳线程数小于默认线程数时，最小线 程数。
	 */
	@Getter
	@Setter
	@Label("最小并发线程数")
	private int keepalivetime = 5;

	/** 规定的当爬虫访问URL失败时候，在当前线程中重新访问的次数。每一次访问会默认随机暂停3-9秒钟。 */
	@Getter
	@Setter
	@Label("请求尝试次数")
	private int retry = 1;

	/** 爬虫执行时的默认并发线程数。 */
	@Getter
	@Setter
	@Label("默认线程并发数")
	private int threads = 5;

	/**
	 * 规定了爬虫在提取页面URL时候的个数，从上到下提取。如果为-1则提取当前页面所有的 URL。
	 */
	@Getter
	@Setter
	@Label("页面广度")
	private int topum = -1;

	public CrawlerAttrInfo formObj(CrawlerConfig config) {
		this.id = config.getId();
		this.name = config.getName();
		this.description = config.getDescription();
		this.seedurl = config.getSeedurl();
		this.depth = config.getDepth();
		this.maxExecuteCount = config.getMaxexecutecount();
		this.maxhreads = config.getMaxthreads();
		this.keepalivetime = config.getKeepalivetime();
		this.isProxy = config.isIsproxy();
		this.retry = config.getRetry();
		this.threads = config.getThreads();
		this.topum = config.getTopnum();
		return this;
	}
}
