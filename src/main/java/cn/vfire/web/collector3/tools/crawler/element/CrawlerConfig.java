package cn.vfire.web.collector3.tools.crawler.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Node;

import com.google.gson.annotations.Expose;

import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;
import lombok.Getter;
import lombok.Setter;

/**
 * 爬虫任务配置
 * 
 * @author ChenGang
 *
 */
public class CrawlerConfig extends Element<CrawlerConfig> {

	private static final long serialVersionUID = 1L;

	private static String[] childNames = new String[] { "name", "description", "seedurl", "regexrules", "unregexrules", "datamode", "event", "snapshot" };

	private static String[] attibutes = new String[] { "id", "depth", "isproxy", "maxexecutecount", "maxthreads", "keepalivetime", "retry", "threads", "topnum" };

	@Setter
	@Getter
	private CrawlerConfigs crawlerConfigs;

	@Getter
	@Expose
	private String id;

	@Getter
	@Expose
	private int depth = 1;

	@Getter
	@Expose
	private boolean isproxy = false;

	@Getter
	@Expose
	private int maxexecutecount = 5;

	@Getter
	@Expose
	private int maxthreads = 50;

	@Getter
	@Expose
	private int keepalivetime = 5;

	@Getter
	@Expose
	private int retry = 1;

	@Getter
	@Expose
	private int topnum = -1;

	@Getter
	@Expose
	private int threads = 5;

	@Getter
	@Expose
	private String name;

	@Getter
	@Setter
	@Expose
	private String description;

	@Getter
	@Expose
	private String seedurl;

	@Getter
	@Expose
	private RegexRules regexrules;

	@Getter
	@Expose
	private UnregexRules unregexrules;

	@Getter
	@Setter
	@Expose
	private Snapshot snapshot;

	@Expose
	private List<DataMode> datamode = new ArrayList<DataMode>();

	@Override
	public String[] childNames() {
		return childNames;
	}

	@Override
	public String[] attributes() {
		return attibutes;
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {

		childNode = this.valiChildNode(fname, childNode);
		String value = childNode.getTextContent();

		if (NAME.equals(fname)) {
			this.name = value;
		} else if (DESCRIPTION.equals(fname)) {
			this.description = value;
		} else if (SEEDURL.equals(fname)) {
			this.seedurl = value;
		} else if (REGEXRULES.equals(fname)) {
			this.regexrules = new RegexRules();
			this.regexrules.parse(childNode);
		} else if (UNREGEXRULES.equals(fname)) {
			this.unregexrules = new UnregexRules();
			this.unregexrules.parse(childNode);
		} else if (DATAMODE.equals(fname)) {
			DataMode dataMode = new DataMode();
			dataMode.parse(childNode);
			this.setDatamode(dataMode);
		} else if (SNAPSHOT.equals(fname)) {
			Snapshot snapshot = new Snapshot();
			snapshot.parse(childNode);
			this.setSnapshot(snapshot);
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {
		if (ID.equals(fname)) {
			fvalue = this.valiAttrNode(fname, fvalue);
			this.id = fvalue;
		}

		try {
			if (DEPTH.equals(fname)) {
				this.depth = Integer.parseInt(fvalue);
			} else if (ISPROXY.equals(fname)) {
				this.isproxy = Boolean.parseBoolean(fvalue);
			} else if (MAXEXECUTECOUNT.equals(fname)) {
				this.maxexecutecount = Integer.parseInt(fvalue);
			} else if (MAXTHREADS.equals(fname)) {
				this.maxthreads = Integer.parseInt(fvalue);
			} else if (KEEPALIVETIME.equals(fname)) {
				this.keepalivetime = Integer.parseInt(fvalue);
			} else if (RETRY.equals(fname)) {
				this.retry = Integer.parseInt(fvalue);
			} else if (THREADS.equals(fname)) {
				this.threads = Integer.parseInt(fvalue);
			} else if (TOPNUM.equals(fname)) {
				this.topnum = Integer.parseInt(fvalue);
			}
		} catch (NumberFormatException e) {
		}
	}

	/**
	 * Crawler任务配置Datamode描述
	 * 
	 * @return
	 */
	public List<DataMode> getDatamode() {
		return Collections.unmodifiableList(datamode);
	}

	public void setDatamode(DataMode datamode) {
		this.datamode.add(datamode);
	}

}
