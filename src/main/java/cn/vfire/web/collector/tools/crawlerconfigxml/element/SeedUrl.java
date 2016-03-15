package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

/**
 * 爬虫种子
 * 
 * @author ChenGang
 *
 */
public class SeedUrl extends Element {

	private static final long serialVersionUID = 1L;

	@Override
	protected String[] parseChildNodeName() {
		return null;
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {
	}

}
