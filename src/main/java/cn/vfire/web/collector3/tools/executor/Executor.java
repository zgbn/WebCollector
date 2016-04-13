package cn.vfire.web.collector3.tools.executor;

import cn.vfire.web.collector3.lang.CrawlerNetException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Page;

/**
 * 请求网页，得到页面响应结果。
 * 
 * @author ChenGang
 *
 */
public interface Executor {

	/**
	 * 该方法根据给定的信息去访问网页，并得到页面响应结果，封装成对象。
	 * 
	 * @param crawlDatum
	 * @return
	 * @throws Exception
	 */
	public Page execute(CrawlDatum crawlDatum) throws CrawlerNetException;

}
