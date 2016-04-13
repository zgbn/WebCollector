package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class TaskStorageException extends CrawlerException {

	private static final long serialVersionUID = 1L;

	public TaskStorageException(CrawlerExpInfo info) {
		super(info);
	}

	public TaskStorageException(CrawlerExpInfo info, Throwable t) {
		super(info, t);
	}

}
