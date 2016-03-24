package cn.vfire.web.collector.tools.crawler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.annotations.Expose;

public abstract class Element<T extends Element<?>> implements ElementInfo, Serializable {

	private static final long serialVersionUID = 1L;

	protected static final Logger log = LoggerFactory.getLogger(Element.class);

	private static Map<String, Element<?>> cacheElementMap = new TreeMap<String, Element<?>>();

	protected static final String FORMATCLASS = "formatclass";
	protected static final String OUTFILE = "outfile";
	protected static final String OUTDATA = "outdata";
	protected static final String CRAWLERCONFIG = "crawlerconfig";
	protected static final String ID = "id";
	protected static final String NODE = "node";
	protected static final String LIST = "list";
	protected static final String KEY = "key";
	protected static final String VALUE = "value";
	protected static final String SELECTER = "selecter";
	protected static final String CLASS = "class";
	protected static final String IP = "ip";
	protected static final String PREFIX = "prefix";
	protected static final String SUFFIX = "suffix";
	protected static final String NAME = "name";
	protected static final String DESCRIPTION = "description";
	protected static final String REGEXRULES = "regexrules";
	protected static final String SEEDURL = "seedurl";
	protected static final String PATH = "path";
	protected static final String REF = "ref";
	protected static final String REGEX = "regex";

	@Expose
	@Getter
	@Setter
	private String id;

	@Getter
	private List<String> ref;

	@Expose
	@Getter
	@Setter
	private String tagname;

	@Expose
	@Getter
	@Setter
	private String text;

	public Element() {
		this.ref = new ArrayList<String>();
		log.debug("创建Element对象。Element:{}", this);
	}

	protected abstract void setFieldByNode(String fname, Node childNode);

	protected abstract void setFieldByAttr(String fname, String fvalue);

	private String getNodeAttributesValue(Node node, String attrName) {
		if (node != null) {
			NamedNodeMap attrs = node.getAttributes();
			if (attrs != null) {
				Node attr = attrs.getNamedItem(attrName);
				if (attr != null) {
					return attr.getNodeValue();
				}
			}
		}
		return null;
	}

	private Node getNodeChild(NodeList nodelist, String childName) {

		int len = nodelist == null ? 0 : nodelist.getLength();

		for (int i = 0; i < len; i++) {

			Node child = nodelist.item(i);

			if (childName.equals(child.getNodeName())) {
				return child;
			}
		}

		return null;

	}

	private void parseNodoAttrs(Node node) {

		if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
			return;
		}

		String[] attrNames = this.attributes();

		for (int i = 0; i < attrNames.length; i++) {

			String attrName = attrNames[i];

			String attrValue = this.getNodeAttributesValue(node, attrName);

			this.setFieldByAttr(attrName, attrValue);

		}

	}

	private void parseNodeChilds(Node node) {

		if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
			return;
		}

		String[] childNodeNames = this.childNames();

		for (int i = 0; i < childNodeNames.length; i++) {

			String childName = childNodeNames[i];

			Node childNode = this.getNodeChild(node.getChildNodes(), childName);

			this.setFieldByNode(childName, childNode);

		}

	}

	/**
	 * 解析节点属性和简单子节点
	 * 
	 * @param node
	 */
	public void parse(Node node) {
		if (node == null) {
			return;
		}

		/* 解析node子节点 */
		if (this.isChildNodes()) {
			this.parseNodeChilds(node);
		}

		/* 解析node属性 */
		if (this.isAttributes()) {
			this.id = this.getNodeAttributesValue(node, ID);
			if (this.id != null && "".equals(this.id) == false) {
				cacheElementMap.put(this.id, this);
			}
			this.parseNodoAttrs(node);
		}

		// 解析标签
		this.tagname = node.getNodeName();
		// 解析文本
		this.text = node.getTextContent();
	}

	public void setRef(String ref) {
		this.ref.add(ref);
	}

	private boolean isChildNodes() {
		return this.childNames().length > 0;
	}

	private boolean isAttributes() {
		return this.attributes().length > 0;
	}

}
