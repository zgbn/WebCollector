package cn.vfire.web.collector3.crawler.snapshot;

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

	@Setter
	private CatchSnapshotInfo catchSnapshotInfo;

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

	public CrawlSnapshot() {

	}

	public int incrementAndGetShapshotExceptionCount() {
		if (this.catchSnapshotInfo != null && this.exceptioncount > 0) {
			synchronized (this.info) {
				this.info.setExceptionCount(this.info.getExceptionCount() + 1);
				if (this.exceptioncount == this.info.getExceptionCount()) {
					this.catchSnapshotInfo.info(info);
					this.info.setExceptionCount(0);
				}
			}
		}
		return this.info.getExceptionCount();
	}

	public int incrementAndGetShapshotRunCount() {
		if (this.catchSnapshotInfo != null && this.count > 0) {
			synchronized (this.info) {
				this.info.setRunCount(this.info.getRunCount() + 1);
				if (this.count == this.info.getRunCount()) {
					this.catchSnapshotInfo.info(info);
					this.info.setRunCount(0);
				}
			}
		}
		return this.info.getRunCount();
	}

	public void incrementShapshotRuntime() {

		if (this.catchSnapshotInfo != null && this.runtime > 0) {

			synchronized (this.info) {

				long threadRuntime = System.currentTimeMillis() - this.info.getInittime();

				if (this.runtime <= threadRuntime) {

					if (this.info.getMinRuntime() > threadRuntime) {
						this.info.setMinRuntime(threadRuntime);
					}

					if (this.info.getMaxRuntime() < threadRuntime) {
						this.info.setMaxRuntime(threadRuntime);
					}

					try {
						this.catchSnapshotInfo.info(info);
					} catch (Throwable e) {
						log.warn("抓取爬虫快照发生异常。", e);
					}

					this.info.setInittime(System.currentTimeMillis());

				}
			}
		}

	}

}
