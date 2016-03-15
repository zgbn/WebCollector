package cn.vfire.web.collector.net;

import lombok.Getter;
import lombok.Setter;

public class ProxyIP extends IP {

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private String password;

	public ProxyIP(String ip, int port) {
		super(ip, port);
	}

	public ProxyIP(String ip, String port) {
		super(ip, port);
	}

}
