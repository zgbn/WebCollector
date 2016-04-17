package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class CrawlerDBException extends CrawlerException {

	private static final long serialVersionUID = 1L;


	public CrawlerDBException(CrawlerExpInfo info) {
		super(info);
	}


	public CrawlerDBException(CrawlerExpInfo info, Throwable t) {
		super(info, t);
	}


	public CrawlerDBException() {
		super(CrawlerExpInfo.FAIL);
	}


	public CrawlerDBException(Throwable t) {
		super(CrawlerExpInfo.FAIL, t);
	}

}
