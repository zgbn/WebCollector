package cn.vfire.web.collector2.tools;

import cn.vfire.web.collector2.crawler.fetcher.Fetcher;
import cn.vfire.web.collector2.lang.CrawlerRuntimeException;
import cn.vfire.web.collector2.lang.enums.CrawlerExpInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * 线程池
 * 
 * @author ChenGang
 *
 */
public abstract class FetcherThreadPool {

	protected Fetcher fetcher;

	@Setter
	protected int initThread = 5;

	@Setter
	protected int minThread = 5;

	@Setter
	protected int maxThread = 20;

	@Setter
	protected int inc = 2;

	@Getter
	private long runtime;


	public FetcherThreadPool(Fetcher fetcher) {
		this.fetcher = fetcher;
	}


	/**
	 * 动态递减并发运行线程数，递减数有this.inc属性控制。
	 */
	public abstract void decrementThread();


	/**
	 * 线程池停止工作
	 */
	public final void stop() {
		this.fetcher.setClose(true);
	}


	/**
	 * 线程池启动
	 */
	public void execute() {

		long time = System.currentTimeMillis();

		this.validate();

		this.run();

		this.runtime = System.currentTimeMillis() - time;
	}


	/**
	 * 线程池实现过程
	 */
	protected abstract void run();


	/**
	 * 动态递增并发运行线程数，递增数有this.inc属性控制。
	 */
	public abstract void incrementThead();


	private void validate() {

		if (this.fetcher == null) {
			throw new CrawlerRuntimeException(
					CrawlerExpInfo.VALIDATE.setInfo("FecherPool参数初始化校验错误。fetcher={}", this.fetcher));
		}

		if (this.minThread >= this.maxThread) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE
					.setInfo("FecherPool参数初始化校验错误。minThread<maxThread is {}", (this.minThread < this.maxThread)));
		}

		if (this.initThread < this.minThread) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE
					.setInfo("FecherPool参数初始化校验错误。initThread<minThread is {}", (this.initThread < this.minThread)));
		}

		if (this.initThread > this.maxThread) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE
					.setInfo("FecherPool参数初始化校验错误。initThread>maxThread is {}", (this.initThread > this.maxThread)));
		}

		if (this.inc > (this.maxThread - this.initThread)) {
			throw new CrawlerRuntimeException(
					CrawlerExpInfo.VALIDATE.setInfo("FecherPool参数初始化校验错误。inc>(maxThread-initThread) is {}",
							(this.inc > (this.maxThread - this.initThread))));
		}

	}

}
