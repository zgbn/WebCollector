package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import lombok.Getter;
import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

public class DataMode extends Element {

	private static final long serialVersionUID = 1L;

	@Getter
	private String id;

	@Getter
	private String selecter;

	@Getter
	private Urls urls;

	@Getter
	private NNode node;

	@Getter
	private NList list;

	@Override
	protected String[] parseChildNodeName() {
		return new String[] { "node", "list" };
	}

	@Override
	protected void parseSpecial(org.w3c.dom.Node node) throws Exception {

	}

}
