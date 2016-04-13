package cn.vfire.web.collector3.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;
import lombok.Getter;
import lombok.Setter;

public class RList extends LinkedList<RNode> {

	private static final long serialVersionUID = 1L;

	private List<Object> list = new LinkedList<Object>();

	@Getter
	@Setter
	private String key;

	@Getter
	@Setter
	private Object value;


	public RList parse(Page page, NList nlist) {

		if (nlist.isValue()) {
			String[] arrayStr = nlist.getValue().split(",");
			list.addAll(Arrays.asList(arrayStr));

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
				}
				else {
					this.list.add(e.text());
				}
			}
			this.setValue(this.list);
			return this;
		}
		else {

			for (NNode _nnode : nlist.getNode()) {
				RNode _rnode = new RNode().parse(page, _nnode);
				this.list.add(_rnode);
			}

			this.setValue(this.list);
			return this;
		}

	}

}
