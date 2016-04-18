package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class FatchStopException extends Exception {

	private static final long serialVersionUID = 1L;

	public FatchStopException() {
		super(String.format("%s:%s", CrawlerExpInfo.STOP.getCode(), CrawlerExpInfo.STOP.getInfo()));
	}

	public FatchStopException(String info, Throwable t) {
		super(String.format("%s:%s", CrawlerExpInfo.STOP.setInfo(info, "")), t);
	}

	public FatchStopException(CrawlerExpInfo info, Throwable t) {
		super(String.format("%s:%s", info.getCode(), info.getInfo()), t);
	}

	public FatchStopException(CrawlerExpInfo info) {
		super(String.format("%s:%s", info.getCode(), info.getInfo()));
	}
}
