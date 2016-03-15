package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

public class RegexRules extends Element {

	private static final long serialVersionUID = 1L;

	public RegexRules() {
	}

	public RegexRules(String text) {
		this.text = text;
	}

	@Override
	protected String[] parseChildNodeName() {
		return null;
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {

		if (node == null) {
			return;
		}

		NodeList regexNodes = node.getChildNodes();

		int regexNodesLength = regexNodes == null ? 0 : regexNodes.getLength();

		for (int i = 0; i < regexNodesLength; i++) {

			Node regexNode = regexNodes.item(i);

			if ("regex".equals(regexNode.getNodeName())) {
				// TODO 有待验证
				String regex = regexNode.getTextContent();

				if (regex != null && "".equals(regex) == false) {

					this.add(new RegexRules(regex));
				}
			}
		}

		System.out.println(this.text);

	}
}
