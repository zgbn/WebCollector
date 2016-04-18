package cn.vfire.web.collector3.model;

import lombok.Getter;
import lombok.Setter;

public class Link {

	@Getter
	@Setter
	private int depth;

	@Getter
	@Setter
	private String url;

	@Override
	public String toString() {
		return this.url;
	}

	public Link(String url, int depth) {
		this.url = url;
		this.depth = depth;
	}

}
