package cn.vfire.web.collector.tools.crawler.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.Element;

import com.google.gson.annotations.Expose;

/**
 * 爬虫输出数据的形式，文件形式的描述
 * 
 * @author ChenGang
 *
 */
public class OutFile extends Element<OutFile> {

	private static final long serialVersionUID = 1L;

	@Expose
	private String id;

	@Expose
	private String prefix;

	@Expose
	private String suffix;

	@Expose
	@Getter
	private String name;

	@Expose
	private String path;

	@Override
	public String[] childNames() {
		return new String[] { "prefix", "suffix", "name", "path" };
	}

	@Override
	public String[] attributes() {
		return new String[] { "id" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) {
		if (PREFIX.equals(fname)) {
			this.prefix = childNode.getTextContent();
		}
		if (SUFFIX.equals(fname)) {
			this.suffix = childNode.getTextContent();
		}
		if (NAME.equals(fname)) {
			this.name = childNode.getTextContent();
		}
		if (PATH.equals(fname)) {
			this.path = childNode.getTextContent();
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
		if (ID.equals(fname)) {
			this.id = fvalue;
		}
	}

}
