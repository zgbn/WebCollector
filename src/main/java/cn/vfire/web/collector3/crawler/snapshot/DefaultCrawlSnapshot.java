package cn.vfire.web.collector3.crawler.snapshot;

import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.tools.crawler.snapshot.CatchSnapshotInfo;
import cn.vfire.web.collector3.tools.crawler.snapshot.CrawlSnapshot;

public class DefaultCrawlSnapshot extends CrawlSnapshot implements Default {

	public DefaultCrawlSnapshot() {
		super.setCount(0);
		super.setExceptioncount(0);
		super.setRuntime(0);
	}

	@Override
	public void setRuntime(long runtime) {
	}

	@Override
	public void setCount(int count) {
	}

	@Override
	public void setExceptioncount(int exceptioncount) {
	}

	@Override
	public void setCatchSnapshotInfo(CatchSnapshotInfo catchSnapshotInfo) {
	}

	@Override
	public int incrementAndGetShapshotExceptionCount() {
		return 0;
	}

	@Override
	public int incrementAndGetShapshotRunCount() {
		return 0;
	}

	@Override
	public void incrementShapshotRuntime() {
	}

}
