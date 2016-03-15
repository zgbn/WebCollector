package cn.vfire.web.collector.crawler.config;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Node implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String selecter;

	@Getter
	@Setter
	private String xPath;

	@Getter
	@Setter
	private String value;

	@Getter
	@Setter
	private Nodes nodes = new Nodes();

	public boolean addNode(Node node) {
		return this.nodes.add(node);
	}

}
