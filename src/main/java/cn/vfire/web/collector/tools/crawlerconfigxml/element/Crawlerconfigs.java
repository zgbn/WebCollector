package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

/**
 * 爬虫任务集合
 * 
 * @author ChenGang
 *
 */
public class Crawlerconfigs extends Element {

	private static final long serialVersionUID = 1L;

	@Getter
	private FormatClass formatclass;

	@Getter
	private CrawlerConfig crawlerconfig;

	@Getter
	private OutData outdata;

	@Getter
	private OutFile outfile;

	@Override
	protected String[] parseChildNodeName() {
		return new String[] { "formatclass", "crawlerconfig", "outfile", "outdata" };
	}

	@Override
	protected void parseSpecial(Node node) {
	}

}
