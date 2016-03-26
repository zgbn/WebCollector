package cn.vfire.web.collector.tools.crawler.element;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector.tools.crawler.Element;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerConfigXmlException;
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
	private List<NNode> node = new ArrayList<NNode>();


	@Override
	public String[] childNames() {
		return new String[] { "node" };
	}


	@Override
	public String[] attributes() {
		return new String[] { "key", "selecter" };
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
	}


	@Override
	public String getId() {
		return null;
	}


	public void setNode(NNode node) {
		this.node.add(node);
	}

}
