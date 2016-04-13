package cn.vfire.web.collector3.tools.crawler.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;
import cn.vfire.web.collector3.tools.crawler.event.CrawlerEvent;

import com.google.gson.annotations.Expose;

public class Event extends Element<Event> {

	private static final long serialVersionUID = 1L;

	@Expose
	@Getter
	private String id;

	@Expose
	@Getter
	private String classes;

	@Getter
	private CrawlerEvent crawlerEvent;

	@Override
	public String[] childNames() {
		return new String[] {};
	}

	@Override
	public String[] attributes() {
		return new String[] { "id", "class" };
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {

		if (ID.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.id = fvalue;
		}

		if (CLASS.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.classes = fvalue;
			try {
				Object obj = Class.forName(fvalue).newInstance();
				if (obj instanceof CrawlerEvent) {
					this.crawlerEvent = (CrawlerEvent) obj;
				} else {
					throw new CrawlerConfigXmlException("Element的class属性描述的类必须实现CrawlerEvent接口。Element:%s class:%s CrawlerEvent:%s", this.getTagname(),
							this.getClasses(), obj.toString());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {
	}

}
