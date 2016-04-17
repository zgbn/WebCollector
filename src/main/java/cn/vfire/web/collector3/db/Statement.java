package cn.vfire.web.collector3.db;

import java.util.Comparator;
import java.util.List;

import cn.vfire.web.collector3.lang.CrawlerDBStatementException;
import cn.vfire.web.collector3.model.CrawlDatum;

/**
 * 数据库动作
 * 
 * @see {@link DBManager} {@link DBLock}
 */
public interface Statement {

	public void flush();


	/**
	 * 初始化写动作，通过传入lock对象，控制数据库动作事务关系。
	 * 
	 * @param lock
	 *            数据库锁，可为null。
	 * @throws Exception
	 */
	public void init(DBLock lock) throws CrawlerDBStatementException;


	/**
	 * 保存一条记录<br />
	 * 如果key不存在则插入，如果存在则替换值。<br />
	 * 注意：此方法内部需要实现事务控制，不然此接口无法保证事务的完整性
	 * 
	 * @param key
	 * @param fetchDatum
	 * @throws CrawlerDBStatementException
	 */
	public void save(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException;


	/**
	 * 保存一条数据<br />
	 * 如果key数据存在则返回false
	 * 
	 * @param fetchDatum
	 * @throws Exception
	 */
	public boolean insert(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException;


	/**
	 * 重写一条数据，key对应的原始数据会被新的数据替换； <br />
	 * 如果key数据不存在则返回false
	 * 
	 * @param fetchDatum
	 * @throws CrawlerDBStatementException
	 */
	public boolean update(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException;


	/**
	 * 删除一条数据<br />
	 * 如果key数据不存在则返回false
	 * 
	 * @param key
	 * @param fetchDatum
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	public boolean delete(String key) throws CrawlerDBStatementException;


	/**
	 * 根据给定正则表达式的key删除数据，返回删除记录条数；
	 * 
	 * @param regularKey
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	public int deleteList(String regularKey) throws CrawlerDBStatementException;


	/**
	 * 查询一条数据<br />
	 * 如果没有key时返回null。
	 * 
	 * @param key
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	public CrawlDatum select(String key) throws CrawlerDBStatementException;


	/**
	 * 查询多条记录<br />
	 * 如果没有结果则返回空List对象<br />
	 * 默认为key字符串自然排序；
	 * 
	 * @param regularKey
	 *            支持正则表达key
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	public List<CrawlDatum> selectList(String regularKey) throws CrawlerDBStatementException;


	/**
	 * 查询多条记录<br />
	 * 如果没有结果则返回空List对象；
	 * 
	 * @param regularKey
	 *            支持正则表达key
	 * @param comparator
	 *            自定义排序规则
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	public List<CrawlDatum> selectList(String regularKey, Comparator<CrawlDatum> comparator)
			throws CrawlerDBStatementException;


	/**
	 * 查询多条记录<br />
	 * 如果没有结果则返回空List对象<br />
	 * 
	 * @param regularKey
	 *            支持正则表达key
	 * @param page
	 *            分页0-N
	 * @param size
	 *            每页数据个数size>0
	 * @param comparator
	 *            自定义排序规则
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	public List<CrawlDatum> selectList(String regularKey, int page, int size, Comparator<CrawlDatum> comparator)
			throws CrawlerDBStatementException;


	/**
	 * 查询多条记录<br />
	 * 如果没有结果则返回空List对象<br />
	 * 默认为key字符串自然排序
	 * 
	 * @param regularKey
	 *            支持正则表达key
	 * @param page
	 *            分页0-N
	 * @param size
	 *            每页数据个数size>0
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	public List<CrawlDatum> selectList(String regularKey, int page, int size) throws CrawlerDBStatementException;


	/**
	 * 关闭
	 */
	public void close();

}
