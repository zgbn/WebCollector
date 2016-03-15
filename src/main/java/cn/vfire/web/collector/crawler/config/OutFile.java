package cn.vfire.web.collector.crawler.config;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class OutFile implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 后缀 */
	private static final String SUFFIX = "suffix";
	/** 前缀 */
	private static final String PREFIX = "prefix";

	@Getter
	@Setter
	private String path;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String dataMode;

	@Getter
	@Setter
	private boolean serialNumber;

	@Getter
	@Setter
	private String position = PREFIX;
	
	

}
