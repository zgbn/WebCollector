package cn.vfire.web.collector.net;

import lombok.Getter;
import lombok.Setter;

public class IP {

	@Getter
	@Setter
	protected String ip;

	@Getter
	protected int port;

	public IP(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public IP(String ip, String port) {
		this.ip = ip;
		this.setPort(port);
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPort(String port) {
		this.port = Integer.parseInt(port);
	}

}
