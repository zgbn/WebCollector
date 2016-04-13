package cn.vfire.web.collector3.db;

import java.util.List;

import cn.vfire.web.collector3.model.CrawlDatum;

/**
 * 爬取过程中，写入爬取历史、网页Content、解析信息的Writer
 */
public interface SegmentWriter {

	/***
	 * 初始化爬去任务写配置
	 * 
	 * @throws Exception
	 */
	public void initSegmentWriter() throws Exception;

	/**
	 * 写入直接取任务
	 * 
	 * @param fetchDatum
	 * @throws Exception
	 */
	public void wrtieFetchSegment(CrawlDatum fetchDatum) throws Exception;

	/**
	 * 写入需要重定向的任务
	 * 
	 * @param datum
	 * @param realUrl
	 * @throws Exception
	 */
	public void writeRedirectSegment(CrawlDatum datum, String realUrl) throws Exception;

	/**
	 * 写入解析后结果的任务集合。
	 * 
	 * @param parseDatums
	 * @throws Exception
	 */
	public void wrtieParseSegment(List<CrawlDatum> parseDatums) throws Exception;

	/**
	 * 关闭写动作。
	 * 
	 * @throws Exception
	 */
	public void closeSegmentWriter() throws Exception;

}
