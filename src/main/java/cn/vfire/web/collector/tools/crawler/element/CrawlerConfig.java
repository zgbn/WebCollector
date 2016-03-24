package cn.vfire.web.collector.tools.crawler.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.common.utils.ReflectUtils;
import cn.vfire.web.collector.crawler.config.DataMode;
import cn.vfire.web.collector.tools.crawler.Element;
import cn.vfire.web.collector.tools.crawler.enums.BeanEnum;

import com.google.gson.annotations.Expose;

/**
 * 爬虫任务配置
 * 
 * @author ChenGang
 *
 */
public class CrawlerConfig extends Element<CrawlerConfig> {

	private static final long serialVersionUID = 1L;

	@Getter
	@Expose
	private String id;

	@Getter
	@Expose
	private int depth = 1;

	@Getter
	@Expose
	private int incthreads = 5;

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
	private int minthreads = 5;

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

	@Expose
	private List<DataMode> datamode = new ArrayList<DataMode>();

	@Override
	public String[] childNames() {
		return new String[] { "name", "description", "seedurl", "regexrules", "unregexrules", "datamode" };
	}

	@Override
	public String[] attributes() {
		return new String[] { "id", "depth", "incthreads", "isproxy", "maxexecutecount", "maxthreads", "minthreads", "retry", "threads", "topnum" };
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) {
		if (NAME.equals(fname) || DESCRIPTION.equals(fname) || SEEDURL.equals(fname)) {
			String value = childNode.getTextContent();
			ReflectUtils.setFieldValue(this, fname, value);
		} else {
			Element<?> fieldElement = BeanEnum.valueOf(fname).newBean();
			fieldElement.parse(childNode);
			ReflectUtils.setFieldValue(this, fname, fieldElement);
		}
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) {
		ReflectUtils.setFieldValue(this, fname, fvalue);
	}

	public List<DataMode> getDatamode() {
		return Collections.unmodifiableList(datamode);
	}

	public void setDatamode(DataMode datamode) {
		this.datamode.add(datamode);
	}

}
