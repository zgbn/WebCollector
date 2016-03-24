package cn.vfire.web.collector.tools.crawler.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Node;

import cn.vfire.web.collector.tools.crawler.Element;
import cn.vfire.web.collector.tools.crawler.enums.BeanEnum;

import com.google.gson.annotations.Expose;

/**
 * 爬虫任务集合
 * 
 * @author ChenGang
 *
 */
public class Crawlerconfigs extends Element<Crawlerconfigs> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Expose
	private List<FormatClass> formatclass = new ArrayList<FormatClass>();
	private List<ProxyIP> proxyip = new ArrayList<ProxyIP>();;
	private List<OutFile> outfile = new ArrayList<OutFile>();
	private List<OutData> outdata = new ArrayList<OutData>();
	private List<CrawlerConfig> crawlerconfig = new ArrayList<CrawlerConfig>();

	@Override
	public String[] childNames() {
		return new String[] { "formatclass", "proxyip", "outfile", "outdata", "crawlerconfig" };
	}

	@Override
	public String[] attributes() {
		return new String[] {};
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) {

		BeanEnum fieldBean = BeanEnum.valueOf(fname);

		Element<?> fieldElement = fieldBean.newBean();

		fieldElement.parse(childNode);

		switch (fieldBean) {
		case formatclass:
			this.setFormatclass((FormatClass) fieldElement);
			break;
		case proxyip:
			this.setProxyip((ProxyIP) fieldElement);
			break;
		case outdata:
			this.setOutdata((OutData) fieldElement);
			break;
		case outfile:
			this.setOutfile((OutFile) fieldElement);
			break;
		case crawlerconfig:
			this.setCrawlerconfig((CrawlerConfig) fieldElement);
			break;
		default:
			break;
		}

	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
	}

	public List<FormatClass> getFormatclass() {
		return Collections.unmodifiableList(formatclass);
	}

	public void setFormatclass(FormatClass formatclass) {
		this.formatclass.add(formatclass);
	}

	public List<ProxyIP> getProxyip() {
		return Collections.unmodifiableList(proxyip);
	}

	public void setProxyip(ProxyIP proxyip) {
		this.proxyip.add(proxyip);
	}

	public List<OutFile> getOutfile() {
		return Collections.unmodifiableList(outfile);
	}

	public void setOutfile(OutFile outfile) {
		this.outfile.add(outfile);
	}

	public List<OutData> getOutdata() {
		return Collections.unmodifiableList(outdata);
	}

	public void setOutdata(OutData outdata) {
		this.outdata.add(outdata);
	}

	public List<CrawlerConfig> getCrawlerconfig() {
		return Collections.unmodifiableList(crawlerconfig);
	}

	public void setCrawlerconfig(CrawlerConfig crawlerconfig) {
		this.crawlerconfig.add(crawlerconfig);

	}

}
