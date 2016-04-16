package cn.vfire.web.collector3.crawler.pool;

import cn.vfire.web.collector3.lang.CrawlerException;
import cn.vfire.web.collector3.model.CrawlDatum;

/**
 * 抓取任务生成器
 */
public interface Generator {

	/**
	 * 获取下一个任务
	 * 
	 * @return
	 */
	public CrawlDatum next();


	/**
	 * 判断是否有下一个
	 * 
	 * @return
	 */
	public boolean hasNext();


	/**
	 * 准备分发任务；
	 * 
	 * @throws CrawlerException
	 */
	public void open() throws CrawlerException;


	/**
	 * 设置每层深度分发任务上限；如果为-1则不设上限；
	 * 
	 * @param topN
	 */
	public void setTopN(int topN);


	/**
	 * 设置同一个任务被分发的最大次数；
	 * 
	 * @param maxExecuteCount
	 */
	public void setMaxExecuteCount(int maxExecuteCount);


	/**
	 * 获取总共分发任务的总数；
	 * 
	 * @return
	 */
	public int getTotalGenerate();


	/**
	 * 停止分发
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;

}
