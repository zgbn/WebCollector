package cn.vfire.web.collector.tools.crawler.element;

import org.w3c.dom.Node;

import cn.vfire.common.utils.ReflectUtils;
import cn.vfire.web.collector.tools.crawler.Element;
import cn.vfire.web.collector.tools.crawler.enums.BeanEnum;
import lombok.Getter;

public class DataMode extends Element<DataMode> {

	private static final long serialVersionUID = 1L;

	@Getter
	private String id;

	@Getter
	private String selecter;

	@Getter
	private Urls urls;

	@Getter
	private OutData outdata;

	@Getter
	private NNode node;

	@Getter
	private NList list;

	@Override
	public String[] childNames() {
		return new String[] { "urls", "outdata", "node", "list" };
	}

	@Override
	public String[] attributes() {
		return new String[] { "id", "selecter" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) {

		if (OUTDATA.equals(fname)) {
			try {
				String ref = childNode.getAttributes().getNamedItem(REF).getNodeValue();
				this.setRef(ref);
			} catch (Exception e) {
				throw new RuntimeException(String.format("Element的ref属性必须存在并且为同Element的ID。Element:%s", childNode.getNodeName()), e);
			}
		} else {
			Element<?> fieldElement = BeanEnum.valueOf(fname).newBean();
			fieldElement.parse(childNode);
			ReflectUtils.setFieldValue(this, fname, fieldElement);
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
		if (ID.equals(fname)) {
			this.id = fvalue;
		}
		if (SELECTER.equals(fname)) {
			this.selecter = fvalue;
		}
	}

}
