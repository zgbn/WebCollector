package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import org.w3c.dom.Node;

import lombok.Getter;
import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

public class NList extends Element {

	private static final long serialVersionUID = 1L;

	@Getter
	private NNode node;

	@Override
	protected String[] parseChildNodeName() {
		return new String[] { "node" };
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {

	}

}
