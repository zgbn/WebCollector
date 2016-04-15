package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class CrawlerNetException extends CrawlerException {

	private static final long serialVersionUID = 1L;


	public CrawlerNetException(CrawlerExpInfo info, Throwable t) {
		super(info, t);
	}


	public CrawlerNetException(CrawlerExpInfo info) {
		super(info);
	}

}
