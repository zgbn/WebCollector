package cn.vfire.web.collector3.crawler.snapshot;

import cn.vfire.web.collector3.tools.Tools;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultCatchSnapshotInfo implements CatchSnapshotInfo {

	@Override
	public void info(Info info) {
		log.debug("爬虫快照信息", Tools.toStringByFieldLabel(info));
	}

}
