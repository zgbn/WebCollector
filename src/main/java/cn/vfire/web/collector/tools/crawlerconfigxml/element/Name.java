package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

/**
 * name
 * @author ChenGang
 *
 */
public class Name extends Element {

	private static final long serialVersionUID = 1L;

	@Override
	protected String[] parseChildNodeName() {
		return null;
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {
	}

}
