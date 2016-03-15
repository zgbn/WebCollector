package cn.vfire.web.collector.tools.crawlerconfigxml.element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawlerconfigxml.Element;

/**
 * 代理IP
 * 
 * @author ChenGang
 *
 */
public class ProxyIP extends Element {

	private static final long serialVersionUID = 1L;

	public ProxyIP() {
	}

	public ProxyIP(String text) {
		this.text = text;
	}

	@Override
	protected String[] parseChildNodeName() {
		return null;
	}

	@Override
	protected void parseSpecial(Node node) throws Exception {

		if (this.text == null || "".equals(this.text)) {
			return;
		}

		Pattern pattern = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}(\\:\\d*)?");

		Matcher matcher = pattern.matcher(this.text);
		// TODO 改完了
		while (matcher.find()) {
			this.add(new ProxyIP(matcher.group()));
		}

	}

}
