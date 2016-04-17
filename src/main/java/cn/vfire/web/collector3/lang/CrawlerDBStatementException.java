package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class CrawlerDBStatementException extends CrawlerDBException {

	private static final long serialVersionUID = 1L;


	public CrawlerDBStatementException() {
		super(CrawlerExpInfo.STATEMENT);
	}


	public CrawlerDBStatementException(Throwable t) {
		super(CrawlerExpInfo.STATEMENT, t);
	}


	public CrawlerDBStatementException(CrawlerExpInfo info, Throwable t) {
		super(info, t);
	}


	public CrawlerDBStatementException(CrawlerExpInfo info) {
		super(info);
	}

}
