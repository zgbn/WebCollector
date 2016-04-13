package cn.vfire.web.collector3.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.select.Elements;

import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;
import lombok.Getter;
import lombok.Setter;

public class RNode {

	@Getter
	private Map<String, Object> map = new LinkedHashMap<String, Object>();

	@Getter
	@Setter
	private String key;

	@Getter
	@Setter
	private Object value;


	public RNode parse(Page page, NNode nnode) {

		this.setKey(nnode.getKey());

		if (nnode.isValue()) {
			this.setValue(nnode.getValue());
			return this;
		}

		if (nnode.isLeaf()) {
			Elements elements = page.select(nnode.getSelecter());
			if (nnode.isAttr()) {
				this.setValue(elements.attr(nnode.getAttr()));
			}
			else {
				this.setValue(elements.text());
			}
			return this;
		}
		else {

			for (NNode _nnode : nnode.getNode()) {
				RNode _rnode = new RNode().parse(page, _nnode);
				this.map.put(_rnode.getKey(), _rnode.getValue());
			}

			for (NList _nlist : nnode.getList()) {
				RList _rlist = new RList().parse(page, _nlist);
				this.map.put(_rlist.getKey(), _rlist.getValue());
			}

			this.setValue(this.map);
			return this;
		}

	}

}
