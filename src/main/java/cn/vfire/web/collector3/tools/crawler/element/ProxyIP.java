package cn.vfire.web.collector3.tools.crawler.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;
import cn.vfire.web.collector3.tools.crawler.proxyip.ProxyIpPool;

import com.google.gson.annotations.Expose;

/**
 * 代理IP final String regex = "(\\d{1,3}\\.){3}\\d{1,3}(\\:\\d*)?";
 * 
 * @author ChenGang
 *
 */
public class ProxyIP extends Element<ProxyIP> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Expose
	private String id;

	@Getter
	@Expose
	private String classes;

	@Getter
	private ProxyIpPool proxyIpPool;

	@Override
	public String[] childNames() {
		return new String[] {};
	}

	@Override
	public String[] attributes() {
		return new String[] { "id", "class" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {

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
				if (obj instanceof ProxyIpPool) {
					this.proxyIpPool = (ProxyIpPool) obj;
					this.proxyIpPool.init();
				} else {
					throw new CrawlerConfigXmlException("Element的class属性描述的类必须继承ProxyIpPool父类。Element:%s class:%s ProxyIpPool:%s", this.getTagname(),
							this.getClasses(), ProxyIpPool.class.getName());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
