package cn.vfire.web.collector3.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;

public class RList implements Iterator<RNode>, Serializable {

	private static final long serialVersionUID = 1L;

	private List<Object> list = new LinkedList<Object>();

	@Getter
	@Setter
	private String key;

	@Getter
	@Setter
	private Object value;

	public RList parse(Page page, NList nlist) {

		this.key = nlist.getKey();

		if (nlist.isValue()) {
			String[] arrayStr = nlist.getValue().split(",");
			for (String str : arrayStr) {
				RNode strNode = new RNode();
				strNode.setValue(str);
				list.add(strNode);
			}

			this.setValue(this.list);
			return this;
		}

		if (nlist.isLeaf()) {
			Elements elements = page.select(nlist.getSelecter());
			int length = elements.size();
			for (int i = 0; i < length; i++) {
				Element e = elements.get(i);
				if (nlist.isAttr()) {
					this.list.add(e.attr(nlist.getAttr()));
				} else {
					this.list.add(e.text());
				}
			}
			this.setValue(this.list);
			return this;
		} else {

			for (NNode _nnode : nlist.getNode()) {
				RNode _rnode = new RNode().parse(page, _nnode);
				this.list.add(_rnode);
			}

			this.setValue(this.list);
			return this;
		}

	}

	@Override
	public boolean hasNext() {
		return this.list.iterator().hasNext();
	}

	@Override
	public RNode next() {
		Object val = this.list.iterator().next();
		RNode node = new RNode();
		node.setKey(this.key);
		node.setValue(val);
		return node;
	}

	@Override
	public void remove() {
		this.list.iterator().remove();
	}

}
