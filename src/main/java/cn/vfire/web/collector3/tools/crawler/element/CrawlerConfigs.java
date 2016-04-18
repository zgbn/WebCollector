package cn.vfire.web.collector3.tools.crawler.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;

/**
 * 爬虫任务集合
 * 
 * @author ChenGang
 *
 */
public class CrawlerConfigs extends Element<CrawlerConfigs> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Expose
	private List<FormatClass> formatclass = new ArrayList<FormatClass>();

	@Expose
	private List<ProxyIP> proxyip = new ArrayList<ProxyIP>();

	@Expose
	private List<OutFile> outfile = new ArrayList<OutFile>();

	@Expose
	private List<OutData> outdata = new ArrayList<OutData>();

	@Expose
	private List<CrawlerConfig> crawlerconfig = new ArrayList<CrawlerConfig>();

	@Override
	public String[] attributes() {
		return new String[] {};
	}

	@Override
	public String[] childNames() {
		return new String[] { "formatclass", "proxyip", "outfile", "outdata", "crawlerconfig", "event" };
	}

	@Override
	public Map<String, Element<?>> getCacheElementMap() {
		return Collections.unmodifiableMap(super.getCacheElementMap());
	}

	public List<CrawlerConfig> getCrawlerconfig() {
		return Collections.unmodifiableList(crawlerconfig);
	}

	/**
	 * 得到所有Crawler任务的id。
	 */
	@Override
	public List<String> getCrawlerIds() {
		return Collections.unmodifiableList(super.getCrawlerIds());
	}

	public List<FormatClass> getFormatclass() {
		return Collections.unmodifiableList(formatclass);
	}

	@Override
	public String getId() {
		return null;
	}

	public List<OutData> getOutdata() {
		return Collections.unmodifiableList(outdata);
	}

	public List<OutFile> getOutfile() {
		return Collections.unmodifiableList(outfile);
	}

	public List<ProxyIP> getProxyip() {
		return Collections.unmodifiableList(proxyip);
	}

	public void setCrawlerconfig(CrawlerConfig crawlerconfig) {
		this.crawlerconfig.add(crawlerconfig);

	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {

		if (FORMATCLASS.equals(fname)) {
			FormatClass fieldElement = new FormatClass();
			fieldElement.parse(childNode);
			this.setFormatclass(fieldElement);
		} else if (PROXYIP.equals(fname)) {
			ProxyIP fieldElement = new ProxyIP();
			fieldElement.parse(childNode);
			this.setProxyip(fieldElement);
		} else if (OUTDATA.equals(fname)) {
			OutData fieldElement = new OutData();
			fieldElement.parse(childNode);
			this.setOutdata(fieldElement);
		} else if (OUTFILE.equals(fname)) {
			OutFile fieldElement = new OutFile();
			fieldElement.parse(childNode);
			this.setOutfile(fieldElement);
		} else if (CRAWLERCONFIG.equals(fname)) {
			childNode = this.valiChildNode(fname, childNode);
			CrawlerConfig fieldElement = new CrawlerConfig();
			fieldElement.parse(childNode);
			fieldElement.setCrawlerConfigs(this);
			this.setCrawlerconfig(fieldElement);
		}

	}

	public void setFormatclass(FormatClass formatclass) {
		this.formatclass.add(formatclass);
	}

	public void setOutdata(OutData outdata) {
		this.outdata.add(outdata);
	}

	public void setOutfile(OutFile outfile) {
		this.outfile.add(outfile);
	}

	public void setProxyip(ProxyIP proxyip) {
		this.proxyip.add(proxyip);
	}

}
