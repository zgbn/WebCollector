package cn.vfire.web.collector.tools.crawlerconfigxml.lang;

public class CrawlerConfigXmlException extends Exception {

	private static final long serialVersionUID = 1L;

	public CrawlerConfigXmlException(String message, Throwable cause) {
		super(message, cause);
	}

	public CrawlerConfigXmlException(Throwable cause) {
		super(cause);
	}

	public CrawlerConfigXmlException(Throwable cause, String message, Object... parames) {
		super((parames != null && parames.length != 0 ? String.format(message, parames) : message), cause);
	}
	
	public CrawlerConfigXmlException(String message, Object... parames) {
		super((parames != null && parames.length != 0 ? String.format(message, parames) : message));
	}

}
