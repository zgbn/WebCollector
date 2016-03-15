package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;
import cn.vfire.web.collector.tools.crawlerconfigxml.format.FormatData;
import cn.vfire.web.collector.tools.crawlerconfigxml.lang.CrawlerConfigXmlException;
import lombok.Getter;

/**
 * 输出数据格式化
 * 
 * @author ChenGang
 *
 */
public class FormatClass extends Element {

	private static final long serialVersionUID = 1L;

	@Getter
	private String id;

	@Getter
	private String classes;

	@Getter
	private String ref;

	@Getter
	private FormatData formatData;

	@Override
	protected String[] parseChildNodeName() {
		return null;
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {

		if (node == null) {
			return;
		}

		Node attr = node.getAttributes().getNamedItem("class");

		if (attr != null) {
			this.classes = attr.getNodeValue();
		}

		if (this.classes != null && "".equals(this.classes) == false) {

			Class<?> clz = Class.forName(this.classes);

			if (FormatData.class.isAssignableFrom(clz)) {

				this.formatData = (FormatData) clz.newInstance();

			} else {

				throw new CrawlerConfigXmlException("解析Node节点class属性时，根据class值描述类必须实现FormatData接口。Node:%s class:%s FormatData:%s", node, this.classes,
						FormatData.class);

			}

		}

	}
}
