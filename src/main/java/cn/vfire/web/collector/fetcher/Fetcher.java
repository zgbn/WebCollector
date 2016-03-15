package cn.vfire.web.collector.fetcher;

import cn.vfire.web.collector.crawler.Generator;

/**
 * 数据采集调度人员
 * 
 * @author ChenGang
 *
 */
public interface Fetcher {

	/**
	 * 调度所有数据采集工人
	 * 
	 * @throws Exception
	 */
	public void fetchAll(Generator generator) throws Exception;

}
