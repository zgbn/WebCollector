package cn.vfire.web.collector.crawler.config;

import java.io.Serializable;

import com.OutDataFormat;

import lombok.Getter;
import lombok.Setter;

//structure
public class DataMode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String selecter;

	@Getter
	@Setter
	private String xPath;

	@Getter
	@Setter
	private Node node;

	@Getter
	@Setter
	private Nodes nodes;

	@Getter
	@Setter
	private OutDataFormat formatClass;
	
	

}
