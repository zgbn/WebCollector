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
	 * 当数据库实例存在并服务处于正常服务中时返回true； <br/>
	 * 当执行{@link DBManager#close()}、 {@link #removeDBServer()}}后，此方法返回false。
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExist();


	/**
	 * 创建一个数据库服务
	 * 
	 * @throws CrawlerDBException
	 *             通常创建数据库失败
	 */
	public void createDBServer() throws CrawlerDBException;


	/**
	 * 删除数据库服务
	 * 
	 * @return
	 * @throws CrawlerDBException
	 *             通常为数据库实例不存在
	 */
	public void removeDBServer() throws CrawlerDBException;


	/**
	 * 清空数据库
	 * 
	 * @return 当清空数据库数据失败的时候返回false，通常为数据库服务被锁了。
	 * @throws CrawlerDBException
	 *             通常为数据库实例不存在
	 * @see {@link DBLock}
	 */
	public boolean clean() throws CrawlerDBException;


	/**
	 * 打开数据库
	 * 
	 * @return 当数据库服务开启失败的时候返回false，开启失败通常为数据库服务被锁了。
	 * @throws CrawlerDBException
	 *             通常为数据库实例不存在
	 * @see {@link DBLock}
	 */
	public boolean open() throws CrawlerDBException;


	/**
	 * 关闭数据库
	 * 
	 * @return 当数据库服务关闭失败的时候返回false，关闭失败通常为数据库服务被锁了。
	 * @throws CrawlerDBException
	 *             通常为数据库实例不存在
	 * @see {@link DBLock}
	 */
	public boolean close() throws CrawlerDBException;

}
