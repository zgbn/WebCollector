package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import lombok.Getter;
import lombok.Setter;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;
import cn.vfire.web.collector.tools.crawlerconfigxml.format.FormatData;

public class ElementRef extends Element {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String fieldname;

	@Getter
	@Setter
	private String ref;

	@Getter
	@Setter
	private String classes;

	@Getter
	@Setter
	private Element element;

	@Override
	protected String[] parseChildNodeName() {
		return null;
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {
	}

}
