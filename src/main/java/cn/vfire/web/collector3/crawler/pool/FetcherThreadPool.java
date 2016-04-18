package cn.vfire.web.collector3.crawler.pool;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector3.crawler.Fetcher;
import cn.vfire.web.collector3.crawler.ware.AbsCrawlerInfoWare;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.lang.CrawlerRuntimeException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.Tools;

/**
 * 线程池,具体的实现需要子类实现。
 * 
 * @author ChenGang
 *
 */
@Slf4j
public abstract class FetcherThreadPool extends AbsCrawlerInfoWare implements CrawlerInfoWare {

	@Setter
	protected Fetcher fetcher;

	protected int initThread = 5;

	protected int keepAliveTime = 5;

	protected int maxThread = 20;

	@Getter
	private long runtime;

	/** 爬虫的名字 */
	protected String name;

	protected String seedUrl;

	protected int snapshotTime;

	protected int snapshotSize;

	/**
	 * 线程池启动
	 */
	public void execute() {

		log.info("爬虫{}开始校验线程池参数。", this.name);

		this.validate();

		log.info("爬虫{}线程池开始运行，开始时间{}。", this.name, Tools.dateCurrentTime());

		long time1 = System.currentTimeMillis();

		this.run();

		long time2 = System.currentTimeMillis();

		this.runtime = time2 - time1;

		log.info("爬虫{}线程池运行结束，结束时间{}，总耗时{}。", this.name, Tools.dateCurrentTime(), Tools.TimeFormat.AUTO.format(this.runtime));
	}

	public abstract int getActiveThreads();

	public int getTotalCount() {
		return this.fetcher.getTotalCount();
	}

	/**
	 * 线程池实现过程
	 */
	protected abstract void run();

	@Override
	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo) {

		this.initThread = crawlerAttrInfo.getThreads();

		this.keepAliveTime = crawlerAttrInfo.getKeepalivetime();

		this.maxThread = crawlerAttrInfo.getMaxhreads();

	}

	private void validate() {

		if (this.fetcher == null) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE.setInfo("FecherPool参数初始化校验错误。fetcher={}", this.fetcher));
		}

		if (this.keepAliveTime < 1) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE.setInfo("FecherPool参数初始化校验错误。keepAliveTime>=1 is {}", (this.keepAliveTime >= 1)));
		}

		if (this.initThread < 1) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE.setInfo("FecherPool参数初始化校验错误。initThread>=1 is {}", (this.initThread >= 1)));
		}

		if (this.initThread > this.maxThread) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE.setInfo("FecherPool参数初始化校验错误。initThread<=maxThread is {}",
					(this.initThread <= this.maxThread)));
		}

	}

}
