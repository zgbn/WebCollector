package cn.vfire.web.collector3.db;

import cn.vfire.web.collector3.lang.CrawlerDBException;

/**
 * 任务库操作标准接口
 * 
 * @author ChenGang
 *
 */
public interface DBManager {

	/**
	 * 创建数据库实例的statiment
	 * 
	 * @param name
	 * @return
	 */
	public Statement createStatement(String name);


	/**
	 * 实现此接口的数据库的产品名字
	 * 
	 * @return
	 */
	public String getProductName();


	/**
	 * 当数据库实例存在并服务处于正常服务中时返回true； <br/>
	 * 当执行{@link DBManager#close()}、 {@link #removeDBServer()}}后，此方法返回false。
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExist();


	/**
	 * 清空数据库
	 * 
	 * @throws CrawlerDBException
	 *             通常为数据库实例不存在
	 * @see {@link DBLock}
	 */
	public void clean() throws CrawlerDBException;


	/**
	 * 打开数据库
	 * 
	 * @throws CrawlerDBException
	 *             通常为数据库实例不存在
	 */
	public void open() throws CrawlerDBException;


	/**
	 * 关闭数据库
	 * 
	 * @throws CrawlerDBException
	 *             通常为数据库实例不存在
	 */
	public void close() throws CrawlerDBException;

}
