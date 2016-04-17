package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class CrawlerDBLockException extends CrawlerDBStatementException {

	private static final long serialVersionUID = 1L;


	public CrawlerDBLockException() {
		super(CrawlerExpInfo.LOCK);
	}


	public CrawlerDBLockException(Throwable t) {
		super(CrawlerExpInfo.LOCK, t);
	}


	public CrawlerDBLockException(CrawlerExpInfo info, Throwable t) {
		super(info, t);
	}

}
