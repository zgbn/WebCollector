package cn.vfire.web.collector3.lang;

import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;

public class FatchStopException extends CrawlerException {

	private static final long serialVersionUID = 1L;

	public FatchStopException() {
		super(CrawlerExpInfo.STOP);
	}

	public FatchStopException(Throwable t) {
		super(CrawlerExpInfo.STOP, t);
	}

	public FatchStopException(String info, Throwable t) {
		super(CrawlerExpInfo.STOP.setInfo(info, ""), t);
	}

	public FatchStopException(CrawlerExpInfo info, Throwable t) {
		super(info, t);
	}

	public FatchStopException(CrawlerExpInfo info) {
		super(info);
	}

}
