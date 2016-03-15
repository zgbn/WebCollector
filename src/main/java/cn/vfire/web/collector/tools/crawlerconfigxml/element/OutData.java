package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

/**
 * 爬虫输出数据描述
 * 
 * @author ChenGang
 *
 */
public class OutData extends Element {

	private static final long serialVersionUID = 1L;

	@Getter
	private ElementRef ref;

	@Getter
	private CrawlerConfig crawlerconfig;

	@Getter
	private FormatClass formatclass;

	@Getter
	private OutFile outfile;

	@Override
	protected String[] parseChildNodeName() {
		return new String[] { "crawlerconfig", "formatclass", "outfile" };
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {
	}
}
