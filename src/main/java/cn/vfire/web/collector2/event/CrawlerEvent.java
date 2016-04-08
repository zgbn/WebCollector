package cn.vfire.web.collector2.event;

import cn.vfire.web.collector2.model.CrawlDatum;
import cn.vfire.web.collector2.model.Page;
import cn.vfire.web.collector2.tools.TaskPool;

/**
 * 爬虫执行任务过程中触发事件。
 * 
 * @author ChenGang
 *
 */
public interface CrawlerEvent {

	/**
	 * 顺序
	 * 
	 * @return
	 */
	public int index();


	/**
	 * 爬虫开始执行之前
	 */
	public void crawlerBefore();


	/**
	 * 爬虫任务结束后
	 */
	public void crawlerAfer();


	/**
	 * 爬虫任务结束后 异步响应
	 */
	public void crawlerAferAsyn();


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
	public boolean facherBefore(CrawlDatum crawlDatum, TaskPool taskPool, int count);


	/**
	 * 采集任务完成之后 异步响应
	 * 
	 * @param page
	 *            抓取到的页面
	 * @param taskPool
	 *            任务池
	 * @param count
	 *            同一个crawlDatum抓取次数
	 */
	public void facherAferAsyn(Page page, TaskPool taskPool, int count);


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
	public boolean facherAfer(Page page, TaskPool taskPool, final int count);


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
	public boolean facherExceptin(CrawlDatum crawlDatum, TaskPool taskPool, Exception e, final int count);


	/**
	 * 开始时触发的事件
	 * 
	 * @param name
	 *            总任务名称
	 * @param serialNumber
	 *            触手编号
	 * @param taskPool
	 *            任务池
	 * @return false则触手将停止执行任务
	 */
	public boolean facherStart(String name, int serialNumber, TaskPool taskPool);


	/**
	 * 全部结束时触发的事件
	 * 
	 * @param name
	 *            总任务名称
	 * @param serialNumber
	 *            触手编号
	 * @param exceptionCount
	 *            触手总共执行多少次任务
	 * @param taskPool
	 *            任务池
	 */
	public void facherEnd(String name, int serialNumber, int exceptionCount, TaskPool taskPool);

}
