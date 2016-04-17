package cn.vfire.web.collector3.crawler.plugin.berkeley;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector.plugin.berkeley.BerkeleyDBUtils;
import cn.vfire.web.collector3.db.DBLock;
import cn.vfire.web.collector3.db.Statement;
import cn.vfire.web.collector3.lang.CrawlerDBLockException;
import cn.vfire.web.collector3.lang.CrawlerDBStatementException;
import cn.vfire.web.collector3.lang.CrawlerRuntimeException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;

public class BerkeleyDBStatement implements Statement, DBLock {

	private static final int BUFFER_SIZE = 50;

	private static final String BerkeleyLock = "BerkeleyLock";

	private AtomicInteger count = new AtomicInteger(0);

	private String dbName;

	private Environment env;

	/** 游标 */
	private Cursor cursor;

	/** 数据库实例 */
	private Database database;

	private String lockKey;


	public BerkeleyDBStatement(String dbName, Environment env) {
		this.dbName = dbName;
		this.env = env;
		this.database = env.openDatabase(null, dbName, BerkeleyDBUtils.defaultDBConfig);
		this.lockKey = String.format("%s-LOCK", dbName.toLowerCase());
	}


	@Override
	public void close() {

		if (this.cursor != null) {
			this.cursor.close();
		}

		if (this.database != null) {
			this.database.sync();
			this.database.close();
		}

	}


	/**
	 * 删除一条数据，如果key不存在则不执行删除动作，并返回false。
	 * 
	 * @param key
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	@Override
	public boolean delete(String key) throws CrawlerDBStatementException {

		Database database = this.getDatabase();

		DatabaseEntry dbkey = null, dbvalue = null;

		try {

			dbkey = this.toDatabaseEntry(key);

			dbvalue = this.toDatabaseEntry();

			// 数据存在
			if (database.get(null, dbkey, dbvalue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

				database.delete(null, dbkey);

				return true;
			}

			return false;

		}
		catch (Exception e) {

			throw new CrawlerDBStatementException(CrawlerExpInfo.STATEMENT_UPDATE, e);

		}

	}


	/**
	 * 此方法不建议使用，性能不高，内部采用全部遍历后删除匹配的key。
	 * 
	 * @param regularKey
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	@Override
	public int deleteList(String regularKey) throws CrawlerDBStatementException {

		Database database = env.openDatabase(null, this.dbName, BerkeleyDBUtils.defaultDBConfig);

		Cursor cursor = database.openCursor(null, CursorConfig.DEFAULT);

		DatabaseEntry dbkey = null, dbvalue = null;

		int deleteCount = 0;

		try {

			dbkey = this.toDatabaseEntry();

			dbvalue = this.toDatabaseEntry();

			deleteCount = 0;

			while (cursor.getNext(dbkey, dbvalue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

				String key = this.fromDatabaseEntry(dbkey);

				if (this.matchingKey(key, regularKey)) {

					this.delete(key);

					deleteCount++;

				}
			}
		}
		catch (DatabaseException e) {

			throw new CrawlerDBStatementException(CrawlerExpInfo.STATEMENT_DELETE, e);

		}
		finally {

			cursor.close();

			database.close();

		}

		return deleteCount;
	}


	/**
	 * 将DatabaseEntry对象转换为String
	 * 
	 * @param databaseEntry
	 * @return
	 */
	private String fromDatabaseEntry(DatabaseEntry databaseEntry) {
		try {
			String rs = new String(databaseEntry.getData(), "utf-8");
			return rs;
		}
		catch (Exception e) {
			throw new CrawlerRuntimeException(e);
		}
	}


	/**
	 * 获取数据库对象实体
	 * 
	 * @return
	 */
	public Database getDatabase() {
		if (this.database == null) {
			this.database = env.openDatabase(null, this.dbName, BerkeleyDBUtils.defaultDBConfig);
		}
		return database;
	}


	@Override
	public void init(DBLock lock) throws CrawlerDBStatementException {
		if (lock.isLocked()) {
			lock.unlock();
		}
	}


	/**
	 * 保存一条数据，如果key存在则不会执行保存并返回false。
	 * 
	 * @param key
	 * @param fetchDatum
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	@Override
	public boolean insert(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException {

		Database database = this.getDatabase();

		DatabaseEntry dbkey = null, dbvalue = null;

		try {

			dbkey = this.toDatabaseEntry(key);

			dbvalue = this.toDatabaseEntry(SerializeUtils.serializeForJson(fetchDatum, CrawlDatum.class));

			OperationStatus status = database.get(null, dbkey, this.toDatabaseEntry(), LockMode.DEFAULT);

			if (status == OperationStatus.NOTFOUND) {

				database.put(null, dbkey, dbvalue);

				this.count.incrementAndGet();

				return true;

			}

			return false;

		}
		catch (Exception e) {

			throw new CrawlerDBStatementException(CrawlerExpInfo.STATEMENT_INSERT, e);

		}
		finally {

			this.sync();

		}

	}


	/**
	 * 正则匹配
	 * 
	 * @param key
	 * @param regex
	 * @return
	 */
	private boolean matchingKey(String key, String regex) {
		boolean flag = Pattern.matches(regex, key);
		return flag;
	}


	/**
	 * 保存一条数据，如果key存在则覆盖，不存在则保存新的。
	 * 
	 * @param key
	 * @param fetchDatum
	 * @throws CrawlerDBStatementException
	 */
	@Override
	public void save(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException {

		Database database = this.getDatabase();

		DatabaseEntry dbkey = null, dbvalue = null;

		try {

			dbkey = this.toDatabaseEntry(key);

			dbvalue = this.toDatabaseEntry(SerializeUtils.serializeForJson(fetchDatum, CrawlDatum.class));

			database.put(null, dbkey, dbvalue);

			this.count.incrementAndGet();

		}
		catch (Exception e) {

			throw new CrawlerDBStatementException(CrawlerExpInfo.STATEMENT_INSERT, e);

		}
		finally {

			this.sync();

		}

	}


	/**
	 * 查询一条数据，如果不存在则返回null。
	 * 
	 * @param key
	 * @return
	 * @throws CrawlerDBStatementException
	 */
	@Override
	public CrawlDatum select(String key) throws CrawlerDBStatementException {

		Database database = this.getDatabase();

		DatabaseEntry dbkey = null, dbvalue = null;

		try {
			dbkey = this.toDatabaseEntry(key);

			dbvalue = this.toDatabaseEntry();

			// 数据存在
			if (database.get(null, dbkey, dbvalue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

				String value = this.fromDatabaseEntry(dbvalue);

				CrawlDatum crawlDatum = SerializeUtils.deserializeFromJson(value, CrawlDatum.class);

				return crawlDatum;
			}

			return null;

		}
		catch (Exception e) {

			throw new CrawlerDBStatementException(CrawlerExpInfo.STATEMENT_UPDATE, e);

		}

	}


	/**
	 * 查询所有key匹配给定正则的数。不建议使用，内部以遍历所有数据后匹配得到结果。
	 * 
	 * @param regularKey
	 *            正则
	 * @see cn.vfire.web.collector3.db.Statement#selectList(java.lang.String)
	 */
	@Override
	public List<CrawlDatum> selectList(String regularKey) throws CrawlerDBStatementException {

		Database database = this.env.openDatabase(null, this.dbName, BerkeleyDBUtils.defaultDBConfig);

		Cursor cursor = database.openCursor(null, CursorConfig.DEFAULT);

		DatabaseEntry dbkey = null, dbvalue = null;

		List<CrawlDatum> rlist = new LinkedList<CrawlDatum>();

		try {

			dbkey = this.toDatabaseEntry();

			dbvalue = this.toDatabaseEntry();

			while (cursor.getNext(dbkey, dbvalue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

				String key = this.fromDatabaseEntry(dbkey);

				if (this.matchingKey(key, regularKey)) {

					rlist.add(SerializeUtils.deserializeFromJson(this.fromDatabaseEntry(dbvalue), CrawlDatum.class));

				}
			}
		}
		catch (DatabaseException e) {

			throw new CrawlerDBStatementException(CrawlerExpInfo.STATEMENT_SELECT, e);

		}
		finally {

			cursor.close();

			database.close();

		}

		return rlist;
	}


	/**
	 * 查询所有key匹配给定正则的数，并按comparator因子从小到大排序。不建议使用，内部以遍历所有数据后匹配得到结果。
	 * 
	 * @see cn.vfire.web.collector3.db.Statement#selectList(java.lang.String,
	 *      java.util.Comparator)
	 */
	@Override
	public List<CrawlDatum> selectList(String regularKey, Comparator<CrawlDatum> comparator)
			throws CrawlerDBStatementException {

		List<CrawlDatum> rlist = this.selectList(regularKey);

		Collections.sort(rlist, comparator);

		return rlist;
	}


	private Cursor getCursor() {
		if (this.cursor == null) {
			this.cursor = this.getDatabase().openCursor(null, CursorConfig.DEFAULT);
		}
		return this.cursor;
	}


	/**
	 * 查询所有key匹配给定正则的数，并按comparator因子从小到大排序。不建议使用，内部以遍历所有数据后匹配得到结果。
	 * 
	 * @param regularKey
	 *            正则
	 * @param page
	 *            页码从1开始。
	 * @param size
	 *            每页数据量大于0
	 * 
	 * @see cn.vfire.web.collector3.db.Statement#selectList(java.lang.String,
	 *      int, int)
	 */
	@Override
	public List<CrawlDatum> selectList(String regularKey, int page, int size) throws CrawlerDBStatementException {

		Cursor cursor = this.getCursor();

		if (page <= 0 || size <= 0) {
			throw new CrawlerRuntimeException(
					CrawlerExpInfo.VALIDATE.setInfo("参数page>0 size>0。page={} size={}", page, size));
		}

		int rsCount = 1;

		int start = (page - 1) * size + 1;

		int end = start + size;

		DatabaseEntry dbkey = null, dbvalue = null;

		dbkey = this.toDatabaseEntry();

		dbvalue = this.toDatabaseEntry();

		List<CrawlDatum> rlist = new LinkedList<CrawlDatum>();

		while (cursor.getNext(dbkey, dbvalue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

			String key = this.fromDatabaseEntry(dbkey);

			if (this.matchingKey(key, regularKey)) {

				if (rsCount >= start && rsCount < end) {

					rlist.add(SerializeUtils.deserializeFromJson(this.fromDatabaseEntry(dbvalue), CrawlDatum.class));

					rsCount++;

				}

			}
		}

		return rlist;
	}


	/**
	 * 查询所有key匹配给定正则的数，并按comparator因子从小到大排序。不建议使用，内部以遍历所有数据后匹配得到结果。
	 * 
	 * @param regularKey
	 *            正则
	 * 
	 * @param page
	 *            页码从1开始。
	 * 
	 * @param size
	 *            每页数据量大于0
	 * @param comparator
	 *            排序因子
	 * 
	 * @see cn.vfire.web.collector3.db.Statement#selectList(java.lang.String,
	 *      int, int, java.util.Comparator)
	 */
	@Override
	public List<CrawlDatum> selectList(String regularKey, int page, int size, Comparator<CrawlDatum> comparator)
			throws CrawlerDBStatementException {

		List<CrawlDatum> rlist = this.selectList(regularKey, page, size);

		Collections.sort(rlist, comparator);

		return rlist;
	}


	public void sync() {
		if (this.count.get() % BUFFER_SIZE == 0) {
			this.count.set(0);
			this.database.sync();
		}
	}


	private DatabaseEntry toDatabaseEntry() {
		return new DatabaseEntry();
	}


	private DatabaseEntry toDatabaseEntry(String entry) {
		try {
			if (entry == null) {
				return new DatabaseEntry();
			}
			else {
				return new DatabaseEntry(entry.getBytes("utf-8"));
			}
		}
		catch (Exception e) {
			throw new CrawlerRuntimeException(e);
		}
	}


	/**
	 * 重写一条数据，key对应的原始数据会被新的数据替换； 如果key数据不存在则返回false
	 * 
	 * @see cn.vfire.web.collector3.db.Statement#update(java.lang.String,
	 *      cn.vfire.web.collector3.model.CrawlDatum)
	 */
	@Override
	public boolean update(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException {

		Database database = this.getDatabase();

		DatabaseEntry dbkey = null, dbvalue = null;

		try {
			dbkey = this.toDatabaseEntry(key);

			dbvalue = this.toDatabaseEntry(SerializeUtils.serializeForJson(fetchDatum, CrawlDatum.class));

			// 数据存在
			if (database.get(null, dbkey, this.toDatabaseEntry(), LockMode.DEFAULT) == OperationStatus.SUCCESS) {

				database.put(null, dbkey, dbvalue);

				return true;
			}

			return false;

		}
		catch (Exception e) {

			throw new CrawlerDBStatementException(CrawlerExpInfo.STATEMENT_UPDATE, e);

		}
		finally {
			this.sync();
		}

	}


	@Override
	public void flush() {
		this.sync();
	}


	@Override
	public void lock() throws CrawlerDBLockException {

		Database lock = env.openDatabase(null, BerkeleyLock, BerkeleyDBUtils.defaultDBConfig);

		DatabaseEntry dbkey = null, dbvalue = null;

		try {

			dbkey = this.toDatabaseEntry(this.lockKey);

			dbvalue = this.toDatabaseEntry(String.valueOf(true));

			lock.put(null, dbkey, dbvalue);

		}
		catch (Exception e) {

			throw new CrawlerDBLockException(e);

		}
		finally {

			lock.sync();
			lock.close();

		}

	}


	@Override
	public boolean isLocked() throws CrawlerDBLockException {

		Database lock = env.openDatabase(null, BerkeleyLock, BerkeleyDBUtils.defaultDBConfig);

		DatabaseEntry dbkey = null, dbvalue = null;

		try {

			dbkey = this.toDatabaseEntry(this.lockKey);

			dbvalue = this.toDatabaseEntry();

			OperationStatus status = lock.get(null, dbkey, dbvalue, LockMode.DEFAULT);

			if (status == OperationStatus.SUCCESS) {

				String rs = this.fromDatabaseEntry(dbvalue);

				return Boolean.parseBoolean(rs);

			}

			return false;

		}
		catch (Exception e) {

			throw new CrawlerDBLockException(e);

		}
		finally {

			lock.sync();
			lock.close();

		}

	}


	@Override
	public void unlock() throws CrawlerDBLockException {

		Database lock = env.openDatabase(null, BerkeleyLock, BerkeleyDBUtils.defaultDBConfig);

		DatabaseEntry dbkey = null, dbvalue = null;

		try {

			dbkey = this.toDatabaseEntry(this.lockKey);

			dbvalue = this.toDatabaseEntry(String.valueOf(false));

			lock.put(null, dbkey, dbvalue);

		}
		catch (Exception e) {

			throw new CrawlerDBLockException(e);

		}
		finally {

			lock.sync();
			lock.close();

		}

	}

}
