package cn.vfire.web.collector.tools.crawler.element;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.Element;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerConfigXmlException;
import lombok.Getter;

import com.google.gson.annotations.Expose;

/**
 * 爬虫输出数据描述
 * 
 * @author ChenGang
 *
 */
public class OutData extends Element<OutData> {

	private static final long serialVersionUID = 1L;

	@Expose
	@Getter
	private String id;

	@Expose
	@Getter
	private FormatClass formatclass;

	@Expose
	@Getter
	private OutFile outfile;

	@Override
	public String[] childNames() {
		return new String[] { "formatclass", "outfile" };
	}

	@Override
	public String[] attributes() {
		return new String[] { "id" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {
		if (FORMATCLASS.equals(fname) || OUTFILE.equals(fname)) {
			childNode = this.valiChildNode(fname, childNode, REF) ;
			String ref = childNode.getAttributes().getNamedItem(REF).getNodeValue();
			this.setRef(ref);
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {
		if (ID.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.id = fvalue;
		}
	}


}
