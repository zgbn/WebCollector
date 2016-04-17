package cn.vfire.web.collector3.db;

import cn.vfire.web.collector3.lang.CrawlerDBLockException;

/**
 * 数据库操作锁管理接口
 * 
 * @see {@link DBManager } {@link Statement}
 */
public interface DBLock {

	/**
	 * 获取锁
	 * 
	 * @throws Exception
	 */
	public void lock() throws CrawlerDBLockException;


	/**
	 * 判断是否被锁
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isLocked() throws CrawlerDBLockException;


	/**
	 * 解锁
	 * 
	 * @throws Exception
	 */
	public void unlock() throws CrawlerDBLockException;
}
