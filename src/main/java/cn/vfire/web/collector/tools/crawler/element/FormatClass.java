package cn.vfire.web.collector.tools.crawler.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.Element;
import cn.vfire.web.collector.tools.crawler.format.FormatData;
import cn.vfire.web.collector.tools.crawler.lang.CrawlerConfigXmlException;

import com.google.gson.annotations.Expose;

/**
 * 输出数据格式化
 * 
 * @author ChenGang
 *
 */
public class FormatClass extends Element<FormatClass> {

	private static final long serialVersionUID = 1L;

	@Expose
	@Getter
	private String id;

	@Expose
	@Getter
	private String classes;

	@Getter
	private FormatData formatData;

	@Override
	public String[] childNames() {
		return new String[] {};
	}

	@Override
	public String[] attributes() {
		return new String[] { "id", "classes" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) {
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {

		if (ID.equals(fname)) {
			this.id = fvalue;
		}

		if (CLASS.equals(fname)) {
			this.classes = fvalue;
			try {
				Object obj = Class.forName(fvalue).newInstance();
				if (obj instanceof FormatData) {
					this.formatData = (FormatData) obj;
				} else {
					throw new CrawlerConfigXmlException("Element的class属性描述的类必须实现FormatData接口。Element:%s class:%s FormatData:%s", this.getTagname(),
							this.getClasses(), FormatData.class.getName());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}
}
