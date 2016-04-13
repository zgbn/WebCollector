package cn.vfire.web.collector3.tools.crawler.element;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;

public class NNode extends Element<NNode> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Expose
	private String key;

	@Getter
	@Expose
	private String value;

	@Getter
	@Expose
	private String selecter;

	@Getter
	@Expose
	private String label;

	@Getter
	@Expose
	private String attr;

	@Getter
	@Expose
	private List<NNode> node = new ArrayList<NNode>();

	@Getter
	@Expose
	private List<NList> list = new ArrayList<NList>();


	public boolean isLeaf() {
		return this.node.isEmpty() && this.list.isEmpty();
	}


	public boolean isValue() {
		return this.value != null && "".equals(this.value) == false;
	}


	public boolean isAttr() {
		return this.attr != null && "".equals(this.attr) == false;
	}


	@Override
	public String[] childNames() {
		return new String[] { "node", "list" };
	}


	@Override
	public String[] attributes() {
		return new String[] { "key", "value", "selecter", "attr" };
	}


	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {
		if (NODE.equals(fname)) {
			NNode node = new NNode();
			node.parse(childNode);
			this.setNode(node);
		}
		if (LIST.equals(fname)) {
			NList list = new NList();
			list.parse(childNode);
			this.setList(list);
		}
	}


	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {
		if (KEY.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.key = fvalue;
		}
		else if (VALUE.equals(fname)) {
			this.value = fvalue;
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
	}


	@Override
	public String getId() {
		return null;
	}


	public void setNode(NNode node) {
		this.node.add(node);
	}


	public void setList(NList list) {
		this.list.add(list);
	}


	@Override
	protected String valiAttrNode(String attrName, String attrValue) throws CrawlerConfigXmlException {
		if (LIST.equals(this.getParentTagname())) {
			return attrValue;
		}
		return super.valiAttrNode(attrName, attrValue);
	}

}
