package cn.vfire.web.collector3.db;

/**
 * 数据库操作锁管理接口
 */
public interface DBLock {

	/**
	 * 获取锁
	 * 
	 * @throws Exception
	 */
	public void lock() throws Exception;

	/**
	 * 判断是否被锁
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isLocked() throws Exception;

	/**
	 * 解锁
	 * 
	 * @throws Exception
	 */
	public void unlock() throws Exception;
}
