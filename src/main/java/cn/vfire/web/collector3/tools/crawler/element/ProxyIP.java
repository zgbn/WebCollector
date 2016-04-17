package cn.vfire.web.collector3.tools.crawler.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;
import lombok.Getter;
import lombok.Setter;

/**
 * 代理IP final String regex = "(\\d{1,3}\\.){3}\\d{1,3}(\\:\\d*)?";
 * 
 * @author ChenGang
 *
 */
public class ProxyIP extends Element<ProxyIP> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Expose
	private String id;

	@Expose
	private List<IP> ip = new ArrayList<IP>();


	@Override
	public String[] childNames() {
		return new String[] { "ip" };
	}


	@Override
	public String[] attributes() {
		return new String[] { "id" };
	}


	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {

		if (IP.equals(fname)) {

			String regex = "(\\d{1,3}\\.){3}\\d{1,3}(\\:\\d*)?";

			String ip = childNode.getTextContent();

			if (Pattern.matches(regex, ip)) {
				this.setIp(ip);
			}
			else {
				throw new CrawlerConfigXmlException("Node配置值错误。Node:%s ip:%s", this.getTagname(), ip);
			}

		}

	}


	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {
		if (ID.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.id = fvalue;
		}
	}


	public List<IP> getIp() {
		return Collections.unmodifiableList(ip);
	}


	public void setIp(String ip) {

		StringBuffer tmp = new StringBuffer(ip);

		int idx = tmp.indexOf(":");

		String _ip = tmp.substring(0, idx);

		String _port = tmp.substring(idx + 1);

		this.ip.add(new IP(_ip, _port));
	}


	public void setIp(ProxyIP ip) {
		this.ip.addAll(ip.getIp());
	}


	public static class IP {

		@Getter
		@Setter
		private String ip;

		@Getter
		private int port;


		public IP(String ip, String port) {
			this.setIp(ip);
			this.setPort(port);
		}


		public void setPort(String port) {
			this.port = Integer.parseInt(port);
		}

	}

}
