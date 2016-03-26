package cn.vfire.web.collector.tools.crawler.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.Element;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerConfigXmlException;

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
	@Getter
	private String id;

	@Expose
	@Getter
	private String prefix;

	@Expose
	@Getter
	private String suffix;

	@Expose
	@Getter
	private String name;

	@Expose
	@Getter
	private String path;

	@Expose
	@Getter
	private String zip;


	@Override
	public String[] childNames() {
		return new String[] { "prefix", "suffix", "name", "path" };
	}


	@Override
	public String[] attributes() {
		return new String[] { "id", "zip" };
	}


	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {
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
			childNode = this.valiChildNode(fname, childNode);
			this.path = childNode.getTextContent();
		}
	}


	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {
		if (ID.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.id = fvalue;
		}
		if (ZIP.equals(fname)) {
			if ("Gzip".equalsIgnoreCase(fvalue) || "Zip".equalsIgnoreCase(fvalue)) {
				this.zip = fvalue;
			}
		}
	}

}
