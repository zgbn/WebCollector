package cn.vfire.web.collector3.tools.crawler.element;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;
import lombok.Getter;
import lombok.Setter;

public class DataMode extends Element<DataMode> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Expose
	private String id;

	@Getter
	@Expose
	private String selecter;

	@Getter
	@Expose
	private String label;

	@Getter
	@Expose
	private Urls urls;

	/**
	 * Crawler任务配置Datamode.Outdata描述
	 */
	@Getter
	@Setter
	@Expose
	private OutData outdata;

	@Getter
	@Expose
	private List<NNode> node = new ArrayList<NNode>();

	@Getter
	@Expose
	private List<NList> list = new ArrayList<NList>();


	@Override
	public String[] childNames() {
		return new String[] { "urls", "outdata", "node", "list" };
	}


	@Override
	public String[] attributes() {
		return new String[] { "id", "selecter", "label" };
	}


	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {

		if (OUTDATA.equals(fname)) {
			childNode = this.valiChildNode(fname, childNode, REF);
			String ref = childNode.getAttributes().getNamedItem(REF).getNodeValue();
			this.setRef(ref);
		}
		if (URLS.equals(fname)) {
			this.urls = new Urls();
			this.urls.parse(childNode);
		}
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
		if (ID.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.id = fvalue;
		}
		if (SELECTER.equals(fname)) {
			this.selecter = fvalue;
		}
		if (LABEL.equals(fname)) {
			this.label = fvalue;
		}
	}


	public void setNode(NNode node) {
		this.node.add(node);
	}


	public void setList(NList list) {
		this.list.add(list);
	}

}
