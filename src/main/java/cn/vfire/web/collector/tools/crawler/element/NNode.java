package cn.vfire.web.collector.tools.crawler.element;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector.tools.crawler.Element;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerConfigXmlException;

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
	private List<NNode> node = new ArrayList<NNode>();

	@Getter
	@Expose
	private List<NList> list = new ArrayList<NList>();


	@Override
	public String[] childNames() {
		return new String[] { "node", "list" };
	}


	@Override
	public String[] attributes() {
		return new String[] { "key", "value", "selecter" };
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
		if (VALUE.equals(fname)) {
			this.value = fvalue;
		}
		if (SELECTER.equals(fname)) {
			this.selecter = fvalue;
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
		if(LIST.equals(this.getParentTagname())){
			return attrValue ;
		}
		return super.valiAttrNode(attrName, attrValue);		
	}

}
