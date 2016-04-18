package cn.vfire.web.collector3.tools.crawler.proxyip.impl;

import java.util.concurrent.ConcurrentLinkedQueue;

import cn.vfire.web.collector3.tools.crawler.proxyip.ProxyIpPool;

public class DefaultProxyIpPool extends ProxyIpPool {

	@Override
	protected void initProxyIpPool(ConcurrentLinkedQueue<IP> pool) {
		IP ip = new IP("112.126.65.193", 80);
		pool.add(ip);
	}

}
