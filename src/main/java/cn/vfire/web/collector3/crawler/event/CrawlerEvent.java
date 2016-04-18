package cn.vfire.web.collector3.crawler.event;

import cn.vfire.web.collector3.lang.FatchStopException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Page;

/**
 * 爬虫执行任务过程中触发事件。
 * 
 * @author ChenGang
 *
 */
public interface CrawlerEvent {

	/**
	 * 爬虫任务结束后
	 */
	public void crawlerAfer(long runtime, int crawlDatumCount, int activeThreads);

	/**
	 * 爬虫开始执行之前
	 */
	public void crawlerBefore();

	/**
	 * 采集任务完成之后
	 * 
	 * @param crawlDatum
	 *            抓取数据信息对象
	 * @param taskPool
	 *            任务池
	 * @param count
	 *            同一个crawlDatum抓取次数
	 * @return false则触手将停止执行任务
	 */
	public void facherAfer(Page page) throws FatchStopException;

	/**
	 * 采集数据开始之前
	 * 
	 * @param crawlDatum
	 *            抓取数据信息对象
	 * @param taskPool
	 *            任务池
	 * @param count
	 *            同一个crawlDatum抓取次数
	 * @return false则触手将停止执行任务
	 */
	public void facherBefore(CrawlDatum crawlDatum) throws FatchStopException;

	/**
	 * 全部结束时触发的事件
	 * 
	 * @param serialNumber
	 *            触手编号
	 * @param exeCount
	 *            触手总共执行多少次任务
	 * @param taskPool
	 *            任务池
	 */
	public void facherEnd() throws FatchStopException;

	/**
	 * 发生异常
	 * 
	 * @param crawlDatum
	 *            发生异常时候正在处理的抓取数据信息
	 * @param taskPool
	 *            发生异常任务池对象
	 * @param e
	 *            异常对象
	 * @param count
	 *            发生异常次数
	 * @return false则触手将停止执行任务
	 */
	public void facherExceptin(CrawlDatum crawlDatum, Exception e) throws FatchStopException;

	/**
	 * 开始时触发的事件
	 * 
	 * @param serialNumber
	 *            触手编号
	 * @param taskPool
	 *            任务池
	 * @return false则触手将停止执行任务
	 */
	public void facherStart() throws FatchStopException;

	/**
	 * 顺序
	 * 
	 * @return
	 */
	public int index();

}
