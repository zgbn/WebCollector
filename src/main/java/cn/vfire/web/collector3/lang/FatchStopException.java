package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class FatchStopException extends CrawlerRuntimeException {

	private static final long serialVersionUID = 1L;


	public FatchStopException() {
		super(CrawlerExpInfo.STOP);
	}


	public FatchStopException(Throwable t) {
		super(CrawlerExpInfo.STOP, t);
	}

}
