package cn.vfire.web.collector.tools.crawler.element;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.Element;

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
	private String id;

	@Expose
	private FormatClass formatclass;

	@Expose
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
	protected void setFieldByNode(String fname, Node childNode) {
		if (FORMATCLASS.equals(fname) || OUTFILE.equals(fname)) {
			try {
				String ref = childNode.getAttributes().getNamedItem(REF).getNodeValue();
				this.setRef(ref);
			} catch (Exception e) {
				throw new RuntimeException(String.format("Element的ref属性必须存在并且为同Element的ID。Element:%s", childNode.getNodeName()), e);
			}
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
		if (ID.equals(fname)) {
			this.id = fvalue;
		}
	}

}
