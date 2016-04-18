package cn.vfire.web.collector3.model;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.regex.Pattern;

import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.annotation.Label;
import cn.vfire.web.collector3.lang.CrawlerRuntimeException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * 一个Url的抓取信息
 * 
 * @author ChenGang
 *
 */
public class CrawlDatum implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int MaxDepth = 99;

	public static final String keyRegular = "^CrawlKey-[0-9]{2}-[0-9A-Z]{32}$";

	public static final CrawlDatum Stop = new CrawlDatum() {

		private static final long serialVersionUID = 1L;

		@Override
		public String getKey() {
			String md5Url = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";
			return String.format("CrawlKey-%02d-%s", 99, md5Url.toUpperCase());
		}
	};

	public final static int STATUS_DB_UNEXECUTED = 0;

	public final static int STATUS_DB_FAILED = 1;

	public final static int STATUS_DB_SUCCESS = 5;

	@Getter
	@Setter
	private int status = STATUS_DB_UNEXECUTED;

	@Getter
	@Setter
	private HashMap<String, String> metaData = new HashMap<String, String>();

	@Getter
	@Label("URL")
	private String url;

	@Getter
	@Setter
	@Label("代理IP")
	private Proxy proxy;

	/** 执行次数 */
	@Getter
	@Label("执行次数")
	private int exeCount = 0;

	/** 异常次数 */
	@Getter
	@Label("异常次数")
	private int expCount = 0;

	/** 失效 */
	@Getter
	@Setter
	@Label("失效")
	private boolean invalid = false;

	/** 深度 */
	@Getter
	@Label("深度")
	private int depth;

	private CrawlDatum() {
	}

	private void setDepth(int depth) {
		this.depth = depth;
		if (this.depth > MaxDepth || this.depth < 1) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE.setInfo("创建CrawlDatum任务时，该任务遍历深度超出限定的界限0<{}<{}。", depth, MaxDepth));
		}
	}

	public CrawlDatum(Link link) {
		this.url = link.getUrl();
		this.setDepth(link.getDepth());
	}

	public CrawlDatum(String url, int currentDepth) {
		this.url = url;
		this.setDepth(currentDepth);
	}

	public CrawlDatum(String url, Proxy proxy, int currentDepth) {
		this.url = url;
		this.proxy = proxy;
		this.setDepth(currentDepth);
	}

	public CrawlDatum(String url, String proxyIp, int proxyPort, int currentDepth) {
		this.url = url;
		this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
		this.setDepth(currentDepth);
	}

	public int get() {
		return this.exeCount;
	}

	public String getKey() {
		String md5Url = SerializeUtils.md5(this.url);
		return String.format("CrawlKey-%02d-%s", this.depth, md5Url.toUpperCase());
	}

	public int incExeCountAndGet() {
		return this.exeCount = this.exeCount + 1;
	}

	public int incExpCountAndGet() {
		return this.expCount = this.expCount + 1;
	}

	/**
	 * 判断当前任务是否使用代理IP服务
	 * 
	 * @return
	 */
	public boolean isProxy() {
		return this.proxy != null;
	}

	public String meta(String key) {
		return this.metaData.get(key);
	}

	public CrawlDatum meta(String key, String value) {
		this.metaData.put(key, value);
		return this;
	}

	public static boolean matchingKey(String key) {
		boolean flag = Pattern.matches(keyRegular, key);
		return flag;
	}

}
