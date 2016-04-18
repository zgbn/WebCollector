package cn.vfire.web.collector3.model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;

public class ResultData {

	private List<RNode> rnodes = new LinkedList<RNode>();

	private List<RList> rlists = new LinkedList<RList>();


	public Map<String, Object> toResultData() {

		Map<String, Object> map = new LinkedHashMap<String, Object>();

		if (this.rnodes.isEmpty() == false) {
			for (RNode rnode : this.rnodes) {
				map.put(rnode.getKey(), rnode.getValue());
			}
		}

		if (this.rlists.isEmpty() == false) {
			for (RList rlist : this.rlists) {
				map.put(rlist.getKey(), rlist.getValue());
			}
		}

		return map;
	}


	public void addRNode(Page page, NNode nnode) {
		RNode rnode = new RNode().parse(page, nnode);
		this.rnodes.add(rnode);
	}


	public void addRList(Page page, NList nlist) {
		RList rlist = new RList().parse(page, nlist);
		this.rlists.add(rlist);
	}

}
