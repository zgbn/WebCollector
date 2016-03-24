package cn.vfire.web.collector.tools.crawler.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.Element;

public class NNode extends Element<NNode> {

	private static final long serialVersionUID = 1L;

	@Getter
	private String key;

	@Getter
	private String value;

	@Getter
	private String selecter;

	@Getter
	private NNode node;

	@Getter
	private NList list;

	@Override
	public String[] childNames() {
		return new String[] { "node", "list" };
	}

	@Override
	public String[] attributes() {
		return new String[] { "key", "value", "selecter" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) {
		if (NODE.equals(fname)) {
			this.node = new NNode();
			this.node.parse(childNode);
		}
		if (LIST.equals(fname)) {
			this.list = new NList();
			this.list.parse(childNode);
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
		if (KEY.equals(fname)) {
			this.key = fvalue;
		}
		if (VALUE.equals(fname)) {
			this.value = fvalue;
		}
		if (SELECTER.equals(fname)) {
			this.selecter = fvalue;
		}
	}

}
