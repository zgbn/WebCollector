package cn.vfire.web.collector2.lang;

import cn.vfire.web.collector2.lang.enums.CrawlerExpInfo;

public class CrawlerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CrawlerRuntimeException(CrawlerExpInfo info) {
		super(String.format("%s>%s", info.getCode(), info.getInfo()));
	}

	public CrawlerRuntimeException(CrawlerExpInfo info, Throwable t) {
		super(String.format("%s>%s", info.getCode(), info.getInfo()), t);
	}

}
