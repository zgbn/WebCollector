package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class CrawlerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public CrawlerRuntimeException(CrawlerExpInfo info) {
		super(String.format("%s:%s", info.getCode(), info.getInfo()));
	}


	public CrawlerRuntimeException(CrawlerExpInfo info, Throwable t) {
		super(String.format("%s:%s", info.getCode(), info.getInfo()), t);
	}


	public CrawlerRuntimeException(Throwable t) {
		super("CrawlerRuntimeException", t);
	}

}
