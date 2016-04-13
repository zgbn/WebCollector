package cn.vfire.web.collector3.tools.crawler.snapshot;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultCatchSnapshotInfo implements CatchSnapshotInfo {

	@Override
	public void info(Info info) {
		log.info("DefaultCatchSnapshotInfo info {}", info);
	}

}
