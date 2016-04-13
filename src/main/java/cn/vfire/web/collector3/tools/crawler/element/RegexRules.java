package cn.vfire.web.collector3.tools.crawler.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;

public class RegexRules extends Element<RegexRules> {

	private static final long serialVersionUID = 1L;

	@Expose
	private List<String> regex = new ArrayList<String>();

	@Override
	public String[] childNames() {
		return new String[] { "regex" };
	}

	@Override
	public String[] attributes() {
		return new String[] {};
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {
		if (REGEX.equals(fname)) {
			childNode = this.valiChildNode(fname, childNode) ;
			String regex = childNode.getTextContent();
			this.setRegex(regex);
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
	}

	public List<String> getRegex() {
		return Collections.unmodifiableList(regex);
	}

	public void setRegex(String regex) {
		this.regex.add(regex);
	}

	@Override
	public String getId() {
		return null;
	}
}
