package cn.vfire.web.collector3.tools.crawler.proxyip;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.Getter;
import lombok.Setter;
import cn.vfire.web.collector3.lang.FatchStopException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.tools.Tools;
import cn.vfire.web.collector3.tools.crawler.proxyip.impl.DefaultProxyIpPool;

/**
 * 代理IP池子
 * 
 * @author ChenGang
 *
 */
public abstract class ProxyIpPool {

	public static class IP {

		@Getter
		@Setter
		private String ip;

		@Getter
		private int port;

		public IP(String ip, int port) {
			this.setIp(ip);
			this.setPort(port);
		}

		public IP(String ip, String port) {
			this.setIp(ip);
			this.setPort(port);
		}

		public void setPort(int port) {
			this.port = port;
		}

		public void setPort(String port) {
			this.port = Integer.parseInt(port);
		}

	}

	private static ConcurrentLinkedQueue<IP> pool = new ConcurrentLinkedQueue<IP>();

	private static ProxyIpPool proxyIpPool = new DefaultProxyIpPool();

	public static final ProxyIpPool getProxyIpPool() {
		return proxyIpPool;
	}

	public void init() {
		this.initProxyIpPool(pool);
	}

	protected abstract void initProxyIpPool(final ConcurrentLinkedQueue<IP> pool);

	/**
	 * 获取一个Http代理对象
	 * 
	 * @return
	 * @throws FatchStopException
	 */
	public Proxy getProxy() throws FatchStopException {

		IP ip = null;

		while ((ip = pool.poll()) != null) {
			if (Tools.netTelnet(ip.getIp(), ip.port)) {
				pool.offer(ip);
				return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip.getIp(), ip.port));
			}
		}

		throw new FatchStopException(CrawlerExpInfo.VALIDATE.setInfo("没有获取到可用的代理IP，池中可能无可用的代理IP。ProxyIpPool.pool=", pool));

	}

	public void put(String ip, int port) {
		pool.add(new IP(ip, port));
	}
}
