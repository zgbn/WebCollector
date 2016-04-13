package cn.vfire.web.collector3.tools.crawler.element;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;
import lombok.Getter;

public class NList extends Element<NList> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Expose
	private String key;

	@Getter
	@Expose
	private String selecter;

	@Getter
	@Expose
	private String label;

	@Getter
	@Expose
	private String value;

	@Getter
	@Expose
	private String attr;

	@Getter
	@Expose
	private List<NNode> node = new ArrayList<NNode>();


	public boolean isLeaf() {
		return this.node.isEmpty();
	}


	public boolean isValue() {
		return this.value != null && "".equals(this.value) == false;
	}


	public boolean isAttr() {
		return this.attr != null && "".equals(this.attr) == false;
	}


	@Override
	public String[] childNames() {
		return new String[] { "node" };
	}


	@Override
	public String[] attributes() {
		return new String[] { "key", "selecter", "attr", "value" };
	}


	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {
		if (NODE.equals(fname)) {
			NNode node = new NNode();
			node.parse(childNode);
			this.setNode(node);
		}
	}


	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {
		if (KEY.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.key = fvalue;
		}
		else if (SELECTER.equals(fname)) {
			this.selecter = fvalue;
		}
		else if (LABEL.equals(fname)) {
			this.label = fvalue;
		}
		else if (ATTR.equals(fname)) {
			this.attr = fvalue;
		}
		else if (VALUE.equals(fname)) {
			this.value = fvalue;
		}
	}


	@Override
	public String getId() {
		return null;
	}


	public void setNode(NNode node) {
		this.node.add(node);
	}

}
