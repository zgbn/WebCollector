package cn.vfire.web.collector3.model;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;

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

	@Getter
	@Setter
	private HashMap<String, String> metaData = new HashMap<String, String>();

	@Getter
	private String url;

	@Getter
	private Proxy proxy;

	/** 执行次数 */
	@Getter
	private int exeCount = 0;

	/** 异常次数 */
	@Getter
	private int expCount = 0;

	/** 失效 */
	@Getter
	@Setter
	private boolean invalid = false;

	public int incExeCountAndGet() {
		return this.exeCount = this.exeCount + 1;
	}

	public int incexpCountAndGet() {
		return this.expCount = this.expCount + 1;
	}

	public CrawlDatum(String url) {
		this.url = url;
	}

	public CrawlDatum(String url, Proxy proxy) {
		this.url = url;
		this.proxy = proxy;
	}

	public CrawlDatum(String url, String proxyIp, int proxyPort) {
		this.url = url;
		this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
	}

	public CrawlDatum meta(String key, String value) {
		this.metaData.put(key, value);
		return this;
	}

	public String meta(String key) {
		return this.metaData.get(key);
	}

	/**
	 * 判断当前任务是否使用代理IP服务
	 * 
	 * @return
	 */
	public boolean isProxy() {
		return this.proxy != null;
	}

	@Override
	public String toString() {
		return this.url;
	}

	public int get() {
		return this.exeCount;
	}

}
