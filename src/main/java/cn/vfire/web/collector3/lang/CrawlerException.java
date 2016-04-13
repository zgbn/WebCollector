package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class CrawlerException extends Exception {

	private static final long serialVersionUID = 1L;

	public CrawlerException(CrawlerExpInfo info) {
		super(String.format("%s>%s", info.getCode(), info.getInfo()));
	}

	public CrawlerException(CrawlerExpInfo info, Throwable t) {
		super(String.format("%s>%s", info.getCode(), info.getInfo()), t);
	}

}
