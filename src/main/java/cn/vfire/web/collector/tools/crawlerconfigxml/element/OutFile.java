package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;
import lombok.Getter;

/**
 * 爬虫输出数据的形式，文件形式的描述
 * 
 * @author ChenGang
 *
 */
public class OutFile extends Element {

	private static final long serialVersionUID = 1L;

	@Getter
	private String suffix;

	@Getter
	private String prefix;

	@Getter
	private String path;

	@Getter
	private String name;

	@Override
	protected String[] parseChildNodeName() {
		return null;
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {
	}

}
