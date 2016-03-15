package cn.vfire.web.collector.tools.crawlerconfigxml;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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

import cn.vfire.common.utils.ReflectUtils;
import cn.vfire.web.collector.tools.crawlerconfigxml.element.ElementRef;
import cn.vfire.web.collector.tools.crawlerconfigxml.enums.BeanEnum;

public abstract class Element implements Comparable<ElementMethod>, ElementMethod, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(Element.class);

	private static final Map<Object, Object> CacheMap = new TreeMap<Object, Object>();

	private final List<Element> list = new ArrayList<Element>();

	@Getter
	@Setter
	protected String id;

	@Getter
	@Setter
	protected String tagName;

	@Getter
	@Setter
	protected String text;

	private void cacheMapAdd() {

		Object id = ReflectUtils.getFieldValue(this, "id");

		if (id == null || "".equals(id)) {
			id = String.format("%s@%d", this.getClass(), this.hashCode());
		}

		CacheMap.put(id, this);
	}

	/**
	 * 解析node节点并返回自身对象
	 * 
	 * @param node
	 * @return
	 */
	public Element parse(Node node) {

		if (node == null) {
			return null;
		}

		try {

			this.parseNodeAttributes(node.getAttributes());

			this.parseChildNodes(node.getChildNodes());

			this.parseSpecial(node);

			this.tagName = node.getNodeName();

			this.text = node.getTextContent();

			this.cacheMapAdd();

			return this;

		} catch (Exception e) {

			log.error("解析Xml的Node节点对象发生异常。Node:{}", node, e);

			throw new RuntimeException(e);

		}

	}

	/**
	 * 自身解析对应node节点的属性。
	 * 
	 * @param node
	 */
	private void parseNodeAttributes(NamedNodeMap nodeAttrs) {

		int nodeAttrsLength = nodeAttrs == null ? 0 : nodeAttrs.getLength();

		for (int i = 0; i < nodeAttrsLength; i++) {

			Node attr = nodeAttrs.item(i);

			String attrName = attr.getNodeName();
			String attrValue = attr.getNodeValue();

			Field thisField = ReflectUtils.getField(this.getClass(), attrName);
			Object thisFieldValue = ReflectUtils.valueOfBaseType(thisField, attrValue);

			ReflectUtils.setFieldValue(this, thisField, thisFieldValue);

		}
	}

	private void parseChildRefNode(Node childNode) {

		if (childNode == null) {
			return;
		}

		final String ref = "ref";

		Field refField = ReflectUtils.getField(this.getClass(), ref);

		if (refField != null) {

			ElementRef eref = ReflectUtils.getFieldValue(this, refField, ElementRef.class);

			if (eref == null) {
				eref = new ElementRef();
			}

			NodeList refNodes = childNode.getChildNodes();

			int refNodesLength = refNodes == null ? 0 : refNodes.getLength();

			for (int i = 0; i < refNodesLength; i++) {

				Node refNode = childNode.getAttributes().getNamedItem(ref);

				if (refNode != null) {
					// TODO 改完了
					eref.add(new ElementRef().parse(refNode));

					if (eref.size() == 1) {
						eref = (ElementRef) eref.get(0);
					}
				}
			}
			ReflectUtils.setFieldValue(this, refField, eref);
		}
	}

	private void parseChildNodes(NodeList nodelist) {

		String[] childNames = this.parseChildNodeName();

		if (childNames == null || childNames.length == 0) {
			return;
		}

		int nodelistLength = nodelist == null ? 0 : nodelist.getLength();

		// 循环遍历所有子节点node对象
		for (int i = 0; i < nodelistLength; i++) {

			Node childNode = nodelist.item(i);

			// 循环遍历Element对象指定的子节点node名字
			for (int j = 0; j < childNames.length; j++) {

				String childName = childNames[j];

				// 遇到匹配的进行解析
				if (childName.equals(childNode.getNodeName())) {

					// TODO 有待验证

					Element childElement = ReflectUtils.getFieldValue(this, childName, Element.class);

					if (childElement == null) {
						childElement = BeanEnum.valueOf(childName).newBean().parse(childNode);
					}

					childElement.add(BeanEnum.valueOf(childName).newBean().parse(childNode));

					ReflectUtils.setFieldValue(this, childName, childElement);

				}

			}

			// 常规node解析完成后，尝试解析子节点node带有ref引用属性的node
			this.parseChildRefNode(childNode);

		}

	}

	/**
	 * 解析下级子节点
	 * 
	 * @param node
	 */
	protected abstract String[] parseChildNodeName();

	/**
	 * 不同的节点特殊解析<br/>
	 * 父类会优先执行#parseNodeAttributes()、#parseChildNodes()方法，然后在执行此方法。
	 * 
	 * @see #parseNodeAttributes(NamedNodeMap)
	 * @see #parseChildNodes(NodeList)
	 * @param node
	 *            对自身对应的node节点
	 * @return
	 * 
	 */
	protected abstract void parseSpecial(Node node) throws Exception;

	protected void setAll(Element e) {
		ReflectUtils.setFieldAll(e, this);
	}

	protected final boolean add(Element e) {
		return this.list.add(e);
	}

	protected final boolean addAll(Collection<? extends Element> c) {
		return this.list.addAll(c);
	}

	protected final void clear() {
		this.list.clear();
	}

	protected final boolean contains(Object o) {
		return this.list.contains(o);
	}

	protected final Element get(int index) {
		return this.list.get(index);
	}

	protected final int indexOf(Object o) {
		return this.list.indexOf(o);
	}

	protected final boolean isEmpty() {
		return this.list.isEmpty();
	}

	protected final Iterator<Element> iterator() {
		return this.list.iterator();
	}

	protected final int lastIndexOf(Object o) {
		return this.list.lastIndexOf(o);
	}

	protected final int size() {
		return this.list.size();
	}

	public final boolean isElements() {
		return this.list.size() > 0;
	}

	/**
	 * 返回只读的xml节点对象缓存
	 * 
	 * @return
	 */
	public static final Map<Object, Object> getCacheMap() {
		return Collections.unmodifiableMap(CacheMap);
	}

	/**
	 * 返回只读Elements
	 * 
	 * @return
	 */
	public final List<Element> getElements() {
		return Collections.unmodifiableList(this.list);
	}

	@Override
	public int compareTo(ElementMethod o) {

		if (this.id == o.getId()) {
			return 0;
		}

		return this.id.compareTo(o.getId());
	}

}
