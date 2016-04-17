package cn.vfire.web.collector3.crawler.plugin.berkeley;

import java.util.Comparator;
import java.util.List;

import cn.vfire.web.collector3.crawler.pool.Generator;
import cn.vfire.web.collector3.crawler.pool.TaskPool;
import cn.vfire.web.collector3.db.DBLock;
import cn.vfire.web.collector3.db.DBManager;
import cn.vfire.web.collector3.db.Statement;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.lang.CrawlerDBStatementException;
import cn.vfire.web.collector3.lang.CrawlerException;
import cn.vfire.web.collector3.model.CrawlDatum;

public class BerkeleyTaskPool extends TaskPool implements DBManager, Statement, DBLock {

	@Override
	public CrawlDatum next() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setTopN(int topN) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setMaxExecuteCount(int maxExecuteCount) {
		// TODO Auto-generated method stub

	}


	@Override
	public int getTotalGenerate() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void lock() throws Exception {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean isLocked() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void unlock() throws Exception {
		// TODO Auto-generated method stub

	}


	@Override
	public void init(DBLock lock) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub

	}


	@Override
	public void save(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean insert(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean update(String key, CrawlDatum fetchDatum) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean delete(String key) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int deleteList(String regularKey) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public CrawlDatum select(String key) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<CrawlDatum> selectList(String regularKey) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <T> List<CrawlDatum> selectList(String regularKey, Comparator<T> comparator)
			throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <T> List<CrawlDatum> selectList(String regularKey, int page, int size, Comparator<T> comparator)
			throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<CrawlDatum> selectList(String regularKey, int page, int size) throws CrawlerDBStatementException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isExist() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void createDBServer() throws CrawlerDBException {
		// TODO Auto-generated method stub

	}


	@Override
	public void removeDBServer() throws CrawlerDBException {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean clean() throws CrawlerDBException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Generator getGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

}
