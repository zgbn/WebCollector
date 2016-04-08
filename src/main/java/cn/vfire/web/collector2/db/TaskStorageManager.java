package cn.vfire.web.collector2.db;

import java.util.List;

import cn.vfire.web.collector2.lang.TaskStorageException;
import cn.vfire.web.collector2.model.CrawlDatum;

/**
 * 数据库操作标准接口
 * 
 * @author ChenGang
 *
 */
public interface TaskStorageManager {

	/**
	 * 判断数据服务是否正常的运行中。
	 * 
	 * @return
	 */
	public boolean isStorageExist();

	/**
	 * 创建一个存储仓库，并指定该仓库的ID，如果ID对应的仓库已经存在则返回false。
	 * 
	 * @param id
	 * @return
	 */
	public boolean createStorage(String id) throws TaskStorageException;

	/**
	 * 删除一个仓库，如果不存在则返回false。
	 * 
	 * @return
	 */
	public boolean removeStorage() throws TaskStorageException;

	/**
	 * 打开数据库
	 * 
	 * @throws Exception
	 */
	public void open() throws TaskStorageException;

	/**
	 * 关闭数据库
	 * 
	 * @throws Exception
	 */
	public void close() throws TaskStorageException;

	/**
	 * 保存一个数据
	 * 
	 * @param crawlDatum
	 */
	public void save(CrawlDatum crawlDatum) throws TaskStorageException;

	/**
	 * 保存一组数据
	 * 
	 * @param list
	 */
	public void saveList(List<CrawlDatum> list) throws TaskStorageException;

	/**
	 * 获取一个数据
	 * 
	 * @return
	 */
	public CrawlDatum get() throws TaskStorageException;

	/**
	 * 获取一组数据
	 * 
	 * @param size
	 * @return
	 */
	public List<CrawlDatum> getList(int size) throws TaskStorageException;

}
