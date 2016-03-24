package cn.vfire.web.collector.tools.crawler.element;

import org.w3c.dom.Node;

import cn.vfire.common.utils.ReflectUtils;
import cn.vfire.web.collector.tools.crawler.Element;
import lombok.Getter;

public class NList extends Element<NList> {

	private static final long serialVersionUID = 1L;

	@Getter
	private String key;

	@Getter
	private String selecter;

	@Getter
	private NNode node;

	@Override
	public String[] childNames() {
		return new String[] { "node" };
	}

	@Override
	public String[] attributes() {
		return new String[] { "key", "selecter" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) {
		if (NODE.equals(fname)) {
			this.node = new NNode();
			this.node.parse(childNode);
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
		ReflectUtils.setFieldValue(this, fname, fvalue);
	}

}
