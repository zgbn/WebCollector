package cn.vfire.web.collector3.tools.crawler;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.vfire.common.utils.ReflectUtils;
import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;

import com.google.gson.annotations.Expose;

@Slf4j
public abstract class Element<T extends Element<?>> implements ElementInfo, Serializable {

	private static final long serialVersionUID = 1L;

	private static Map<String, Element<?>> cacheElementMap = new TreeMap<String, Element<?>>();

	private static List<String> crawlerIds = new ArrayList<String>();

	protected static final String FORMATCLASS = "formatclass";

	protected static final String SNAPSHOTINFO = "snapshotinfo";

	protected static final String OUTFILE = "outfile";

	protected static final String OUTDATA = "outdata";

	protected static final String URLS = "urls";

	protected static final String CRAWLERCONFIG = "crawlerconfig";

	protected static final String EVENT = "event";

	protected static final String SNAPSHOT = "snapshot";

	protected static final String ID = "id";

	protected static final String RUNTIME = "runtime";

	protected static final String EXCEPTIONCOUNT = "exceptioncount";

	protected static final String COUNT = "count";

	protected static final String ZIP = "zip";

	protected static final String DEPTH = "depth";

	protected static final String INCTHREADS = "incthreads";

	protected static final String ISPROXY = "isproxy";

	protected static final String PROXYIP = "proxyip";

	protected static final String MAXEXECUTECOUNT = "maxexecutecount";

	protected static final String MAXTHREADS = "maxthreads";

	protected static final String KEEPALIVETIME = "keepalivetime";

	protected static final String RETRY = "retry";

	protected static final String THREADS = "threads";

	protected static final String TOPNUM = "topnum";

	protected static final String NODE = "node";

	protected static final String LIST = "list";

	protected static final String KEY = "key";

	protected static final String VALUE = "value";

	protected static final String SELECTER = "selecter";

	protected static final String LABEL = "label";

	protected static final String ATTR = "attr";

	protected static final String CLASS = "class";

	protected static final String IP = "ip";

	protected static final String PREFIX = "prefix";

	protected static final String SUFFIX = "suffix";

	protected static final String NAME = "name";

	protected static final String DESCRIPTION = "description";

	protected static final String REGEXRULES = "regexrules";

	protected static final String UNREGEXRULES = "unregexrules";

	protected static final String DATAMODE = "datamode";

	protected static final String SEEDURL = "seedurl";

	protected static final String PATH = "path";

	protected static final String REF = "ref";

	protected static final String REGEX = "regex";

	private List<String> ref = new ArrayList<String>();

	private String parentTagname;

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

	protected Map<String, Element<?>> getCacheElementMap() {
		return cacheElementMap;
	}

	protected List<String> getCrawlerIds() {
		return crawlerIds;
	}

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

	protected String getParentTagname() {
		return parentTagname;
	}

	/**
	 * Crawler任务配置Datamode.ref描述
	 * 
	 * @return
	 */
	public List<String> getRef() {
		return ref;
	}

	private boolean isAttributes() {
		return this.attributes().length > 0;
	}

	private boolean isChildNodes() {
		return this.childNames().length > 0;
	}

	/**
	 * 解析节点属性和简单子节点
	 * 
	 * @param node
	 * @throws CrawlerConfigXmlException
	 */
	public void parse(Node node) throws CrawlerConfigXmlException {
		if (node == null) {
			return;
		}

		// 解析标签
		this.tagname = node.getNodeName();
		// 解析文本
		this.text = this.parseNodeText(node);

		{
			Node pNode = node.getParentNode();
			if (pNode != null) {
				this.parentTagname = pNode.getNodeName();
			}
		}

		/* 解析node子节点 */
		if (this.isChildNodes()) {
			this.parseNodeChilds(node);
		}

		/* 解析node属性 */
		if (this.isAttributes()) {
			this.parseNodoAttrs(node);
			String id = this.getId();
			String cacheElementId = id == null || "".equals(id) ? String.format("Element:%d", this.hashCode()) : id;

			cacheElementMap.put(cacheElementId, this);

			if (this instanceof CrawlerConfig) {
				crawlerIds.add(id);
			}
		}

	}

	private void parseNodeChilds(Node node) throws CrawlerConfigXmlException {

		// log.debug("Element解析对应Node类型。Element:{} NodeType:{}",
		// this.getClass(), Node.ELEMENT_NODE);

		if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
			return;
		}

		String[] childNodeNames = this.childNames();

		NodeList childNodes = node.getChildNodes();

		int childNodesLength = childNodes == null ? 0 : childNodes.getLength();

		for (int i = 0; i < childNodeNames.length; i++) {

			String childName = childNodeNames[i];

			for (int j = 0; j < childNodesLength; j++) {

				Node child = childNodes.item(j);

				if (childName.equals(child.getNodeName())) {
					this.setFieldByNode(childName, child);
				}

			}

		}

	}

	private String parseNodeText(Node node) {
		NodeList childNodes = node.getChildNodes();
		int len = childNodes == null ? 0 : childNodes.getLength();
		StringBuffer text = new StringBuffer();
		for (int i = 0; i < len; i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				text.append(child.getTextContent());
			}
		}
		String rs = text.toString().replaceAll("(\n|\r|\t|\\s)+", "");
		return rs;
	}

	private void parseNodoAttrs(Node node) throws CrawlerConfigXmlException {

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

	protected abstract void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException;

	protected abstract void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException;

	public void setRef(String ref) {
		this.ref.add(ref);
	}

	protected void setRefElement(Element<?> refElement) {

		try {
			String fname = refElement.getTagname();
			Field field = this.getClass().getDeclaredField(fname);
			ReflectUtils.setFieldValueBySetter(this, field, refElement);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 配置文件中Node元素的属性必须存在且有值。
	 * 
	 * @param attrName
	 * @param attrValue
	 * @return
	 * @throws CrawlerConfigXmlException
	 */
	protected String valiAttrNode(String attrName, String attrValue) throws CrawlerConfigXmlException {

		if (attrValue == null || "".equals(attrValue)) {
			throw new CrawlerConfigXmlException("配置文件中Node元素的属性必须存在且有值。Node:%s 属性Name:%s 属性Value:%s", this.getTagname(), attrName, attrValue);
		}
		return attrValue;
	}

	/**
	 * 配置文件中Node元素的子节点ChildNode必须存在
	 * 
	 * @param childNodeName
	 * @param childNode
	 * @return
	 * @throws CrawlerConfigXmlException
	 */
	protected Node valiChildNode(String childNodeName, Node childNode) throws CrawlerConfigXmlException {
		if (childNode == null || childNode.getNodeType() != Node.ELEMENT_NODE) {
			throw new CrawlerConfigXmlException("配置文件中Node元素的子节点ChildNode必须存在。Node:%s ChildName:%s", this.getTagname(), childNodeName);
		}
		return childNode;
	}

	/**
	 * 配置文件中Node元素的子节点ChildNode必须存在;
	 * 配置文件中Node元素的子节点ChildNode必须存在指定的ChildAttrName属性值;
	 * 
	 * @param childNodeName
	 * @param childNode
	 * @param childNodeAttrName
	 * @return
	 * @throws CrawlerConfigXmlException
	 */
	protected Node valiChildNode(String childNodeName, Node childNode, String childNodeAttrName) throws CrawlerConfigXmlException {
		if (childNode == null || childNode.getNodeType() != Node.ELEMENT_NODE) {
			throw new CrawlerConfigXmlException("配置文件中Node元素的子节点ChildNode必须存在。Node:%s ChildName:%s", this.getTagname(), childNodeName);
		}

		try {
			String childNodeAttrValue = childNode.getAttributes().getNamedItem(childNodeAttrName).getNodeValue();
			if (childNodeAttrValue == null || "".equals(childNodeAttrValue)) {
				throw new CrawlerConfigXmlException("配置文件中Node元素的子节点ChildNode必须存在指定的ChildAttrName属性值。Node:%s ChildName:%s ChildAttrName:%s", this.getTagname(),
						childNodeName, childNodeAttrName);
			}
		} catch (Exception e) {
			throw new CrawlerConfigXmlException(e);
		}

		return childNode;
	}

	/**
	 * 配置文件中Node元素的子节点ChildNode必须存在指定的ChildAttrName属性值
	 * 
	 * @param childNodeName
	 * @param childNode
	 * @param childNodeAttrName
	 * @return
	 * @throws CrawlerConfigXmlException
	 */
	protected String valiChildNodeAttr(String childNodeName, Node childNode, String childNodeAttrName) throws CrawlerConfigXmlException {

		String childNodeAttrValue = null;

		if (childNode == null || childNode.getNodeType() != Node.ELEMENT_NODE) {
			return childNodeAttrValue;
		}

		try {
			childNodeAttrValue = childNode.getAttributes().getNamedItem(childNodeAttrName).getNodeValue();
			if (childNodeAttrValue == null || "".equals(childNodeAttrValue)) {
				throw new CrawlerConfigXmlException("配置文件中Node元素的子节点ChildNode必须存在指定的ChildAttrName属性值。Node:%s ChildName:%s ChildAttrName:%s", this.getTagname(),
						childNodeName, childNodeAttrName);
			}
		} catch (Exception e) {
			throw new CrawlerConfigXmlException(e);
		}

		return childNodeAttrValue;
	}

}
