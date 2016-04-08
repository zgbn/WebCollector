package cn.vfire.web.collector2.crawler;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 爬虫运行快照
 * 
 * @author ChenGang
 *
 */
@Slf4j
public class CrawlSnapshot {

	public interface CatchSnapshotInfo {
		public void info(final Info info);
	}

	private class Info {
		/** 逻辑运行次数计数器 */
		private int runCount = 0;

		/** 发生异常次数计数器 */
		private int exceptionCount = 0;

		/** 过程中逻辑最短执行时间毫秒 */
		private long minRuntime = 0;

		/** 过程中逻辑最长执行时间毫秒 */
		private long maxRuntime = 0;

		/** 初始化时间 */
		private long inittime = System.currentTimeMillis();

		@Override
		public String toString() {
			return String.format("Obj=%s,inittime=%d,runCount=%d,exceptionCount=%d,minRuntime=%d,maxRuntime=%s", super.toString(), this.inittime,
					this.runCount, this.exceptionCount, this.minRuntime, this.maxRuntime);
		}
	}

	/** 指定运行多少毫秒后抓取一次快照 */
	@Setter
	private long runtime;

	/** 指定运行多少次逻辑后抓取一次快照 */
	@Setter
	private int count;

	/** 指定运行出现多少次异常抓取一次快照 */
	@Setter
	private int exceptioncount;

	private Info info = new Info();

	private final CatchSnapshotInfo catchSnapshotInfo;

	/**
	 * 创建一个快照，指定运行多少次逻辑或运行到指定时间毫秒或出现多少次一次异常时，抓取一次快照。<br />
	 * 可以通过setter方法设置抓取快照策略；
	 * 
	 * @param catchSnapshotInfo
	 *            当满足条件后抓取快照回调接口
	 */
	public CrawlSnapshot(CatchSnapshotInfo catchSnapshotInfo) {
		this.catchSnapshotInfo = catchSnapshotInfo;
	}

	public int incrementAndGetShapshotExceptionCount() {
		if (this.catchSnapshotInfo != null && this.exceptioncount > 0) {
			synchronized (this.info) {
				this.info.exceptionCount = this.info.exceptionCount + 1;
				if (this.exceptioncount == this.info.exceptionCount) {
					this.catchSnapshotInfo.info(info);
					this.info.exceptionCount = 0;
				}
			}
		}
		return this.info.exceptionCount;
	}

	public int incrementAndGetShapshotRunCount() {
		if (this.catchSnapshotInfo != null && this.count > 0) {
			synchronized (this.info) {
				this.info.runCount = this.info.runCount + 1;
				if (this.count == this.info.runCount) {
					this.catchSnapshotInfo.info(info);
					this.info.runCount = 0;
				}
			}
		}
		return this.info.runCount;
	}

	public void incrementShapshotRuntime() {

		if (this.catchSnapshotInfo != null && this.runtime > 0) {

			synchronized (this.info) {

				long threadRuntime = System.currentTimeMillis() - this.info.inittime;

				if (this.runtime <= threadRuntime) {

					if (this.info.minRuntime > threadRuntime) {
						this.info.minRuntime = threadRuntime;
					}

					if (this.info.maxRuntime < threadRuntime) {
						this.info.maxRuntime = threadRuntime;
					}

					try {
						this.catchSnapshotInfo.info(info);
					} catch (Throwable e) {
						log.warn("抓取爬虫快照发生异常。", e);
					}

					this.info.inittime = System.currentTimeMillis();

				}
			}
		}

	}

}
