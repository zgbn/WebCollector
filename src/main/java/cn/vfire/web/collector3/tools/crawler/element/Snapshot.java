package cn.vfire.web.collector3.tools.crawler.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;

import com.google.gson.annotations.Expose;

public class Snapshot extends Element<Snapshot> {

	private static final long serialVersionUID = 1L;

	@Expose
	@Getter
	private int time;

	@Expose
	@Getter
	private int size;

	@Override
	public String[] childNames() {
		return new String[] {};
	}

	@Override
	public String[] attributes() {
		return new String[] { "time", "size" };
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {
		if (TIME.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.time = Integer.parseInt(fvalue);
		}
		if (SIZE.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.size = Integer.parseInt(fvalue);
		}
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {
	}

}
