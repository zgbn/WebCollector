package cn.vfire.web.collector.tools.crawler.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.Element;

import com.google.gson.annotations.Expose;

/**
 * 代理IP final String regex = "(\\d{1,3}\\.){3}\\d{1,3}(\\:\\d*)?";
 * 
 * @author ChenGang
 *
 */
@Log4j
public class ProxyIP extends Element<ProxyIP> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Expose
	private String id;

	@Expose
	private List<String> ip = new ArrayList<String>();

	@Override
	public String[] childNames() {
		return new String[] { "ip" };
	}

	@Override
	public String[] attributes() {
		return new String[] { "id" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) {

		System.out.println(log);

		if (IP.equals(fname)) {

			String regex = "(\\d{1,3}\\.){3}\\d{1,3}(\\:\\d*)?";

			String ip = childNode.getTextContent();

			if (Pattern.matches(regex, ip)) {
				this.setIp(ip);
			} else {
				// TODO
			}

		}

	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
		if (ID.equals(fname)) {
			this.id = fvalue;
		}
	}

	public List<String> getIp() {
		return Collections.unmodifiableList(ip);
	}

	public void setIp(String ip) {
		this.ip.add(ip);
	}

}
