/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.vfire.web.collector.plugin.berkeley;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import cn.vfire.web.collector.crawldb.DBManager;
import cn.vfire.web.collector.crawldb.Generator;
import cn.vfire.web.collector.model.CrawlDatum;
import cn.vfire.web.collector.model.CrawlDatums;
import cn.vfire.web.collector.util.CrawlDatumFormater;
import cn.vfire.web.collector.util.FileUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BerkeleyDB数据库大管家
 */
public class BerkeleyDBManager extends DBManager {

	Logger LOG = LoggerFactory.getLogger(BerkeleyDBManager.class);

	/** kv环境对象，用于创建内存数据库，数据类型特指BerkeleyDB */
	Environment env;
	/** BerkeleyDB的目录位置 */
	String crawlPath;

	/** 任务解析器对象自身副本 */
	BerkeleyGenerator generator = null;

	public int BUFFER_SIZE = 20;

	Database fetchDatabase = null;

	Database linkDatabase = null;

	Database redirectDatabase = null;

	AtomicInteger count_fetch = new AtomicInteger(0);

	AtomicInteger count_link = new AtomicInteger(0);

	AtomicInteger count_redirect = new AtomicInteger(0);

	Database lockDatabase;

	/**
	 * 构造器
	 * 
	 * @param crawlPath
	 */
	public BerkeleyDBManager(String crawlPath) {
		this.crawlPath = crawlPath;
		this.generator = new BerkeleyGenerator(crawlPath);
	}

	@Override
	public void clear() throws Exception {
		File dir = new File(crawlPath);
		if (dir.exists()) {
			FileUtils.deleteDir(dir);
		}
	}

	/**
	 * 父类抽象方法的实现，关闭数据库资源。
	 * 
	 * @see cn.DBManager.hfut.dmic.webcollector.crawldb.DBManager#close()
	 */
	@Override
	public void close() throws Exception {
		env.close();
	}

	@Override
	public void closeSegmentWriter() throws Exception {
		if (fetchDatabase != null) {
			fetchDatabase.sync();
			fetchDatabase.close();
		}
		if (linkDatabase != null) {
			linkDatabase.sync();
			linkDatabase.close();
		}
		if (redirectDatabase != null) {
			redirectDatabase.sync();
			redirectDatabase.close();
		}
	}

	@Override
	public Generator getGenerator() {
		return generator;
	}

	@Override
	public void initSegmentWriter() throws Exception {
		fetchDatabase = env.openDatabase(null, "fetch", BerkeleyDBUtils.defaultDBConfig);
		linkDatabase = env.openDatabase(null, "link", BerkeleyDBUtils.defaultDBConfig);
		redirectDatabase = env.openDatabase(null, "redirect", BerkeleyDBUtils.defaultDBConfig);

		count_fetch = new AtomicInteger(0);
		count_link = new AtomicInteger(0);
		count_redirect = new AtomicInteger(0);
	}

	/**
	 * 重写父类的注入任务对象方法。
	 * 
	 * @param datum
	 *            注入的任务对象
	 * @param force
	 *            false的时候任务去重复处理
	 * 
	 * @see cn.DBManager.hfut.dmic.webcollector.crawldb.DBManager#inject(cn.edu.hfut.dmic.webcollector.model.CrawlDatum,
	 *      boolean)
	 */
	@Override
	public void inject(CrawlDatum datum, boolean force) throws Exception {
		// 获取数据库对象实体
		Database database = env.openDatabase(null, "crawldb", BerkeleyDBUtils.defaultDBConfig);
		// 注入的任务对象序列化为字符串信息
		DatabaseEntry key = BerkeleyDBUtils.strToEntry(datum.getKey());
		DatabaseEntry value = new DatabaseEntry();

		if (!force) {
			if (database.get(null, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				database.close();
				return;
			}
		}
		// 将str对象处理成数据库识别的字节实体，并通过UTF-8编码。
		value = BerkeleyDBUtils.strToEntry(CrawlDatumFormater.datumToJsonStr(datum));
		// 保存数据
		database.put(null, key, value);

		// 同步内存与硬盘数据
		database.sync();

		// 关闭数据库连接
		database.close();
	}

	@Override
	public boolean isDBExists() {
		File dir = new File(crawlPath);
		return dir.exists();
	}

	@Override
	public boolean isLocked() throws Exception {
		boolean isLocked = false;
		lockDatabase = env.openDatabase(null, "lock", BerkeleyDBUtils.defaultDBConfig);
		DatabaseEntry key = new DatabaseEntry("lock".getBytes("utf-8"));
		DatabaseEntry value = new DatabaseEntry();
		if (lockDatabase.get(null, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			String lockInfo = new String(value.getData(), "utf-8");
			if (lockInfo.equals("locked")) {
				isLocked = true;
			}
		}
		lockDatabase.close();
		return isLocked;
	}

	/**
	 * 遍历打印出 所有的任务信息
	 * 
	 * @throws Exception
	 */
	public void list() throws Exception {
		if (env == null) {
			// 开启数据库
			open();
		}
		// 声明游标
		Cursor cursor = null;
		// 创建数据实体对象，采用默认的数据库配置
		Database crawldbDatabase = env.openDatabase(null, "crawldb", BerkeleyDBUtils.defaultDBConfig);
		// 从数据库实体对象获取操作游标
		cursor = crawldbDatabase.openCursor(null, CursorConfig.DEFAULT);

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();

		/* 循环向下移动游标 */
		while (cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			// 当向下移动游标正常，则直接将游标执行的key、value值对其传入的DatabaseEntry类型的key、value赋值操作。
			try {
				// 从数据库取得任务信息，转化为任务对象
				CrawlDatum datum = BerkeleyDBUtils.createCrawlDatum(key, value);
				System.out.println(CrawlDatumFormater.datumToString(datum));
			} catch (Exception ex) {
				LOG.info("Exception when generating", ex);
				continue;
			}
		}

	}

	@Override
	public void lock() throws Exception {
		lockDatabase = env.openDatabase(null, "lock", BerkeleyDBUtils.defaultDBConfig);
		DatabaseEntry key = new DatabaseEntry("lock".getBytes("utf-8"));
		DatabaseEntry value = new DatabaseEntry("locked".getBytes("utf-8"));
		lockDatabase.put(null, key, value);
		lockDatabase.sync();
		lockDatabase.close();
	}

	@Override
	public void merge() throws Exception {
		LOG.info("start merge");
		Database crawldbDatabase = env.openDatabase(null, "crawldb", BerkeleyDBUtils.defaultDBConfig);
		/* 合并fetch库 */
		LOG.info("merge fetch database");
		Database fetchDatabase = env.openDatabase(null, "fetch", BerkeleyDBUtils.defaultDBConfig);

		Cursor fetchCursor = fetchDatabase.openCursor(null, null);

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();

		while (fetchCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			crawldbDatabase.put(null, key, value);
		}
		fetchCursor.close();
		fetchDatabase.close();

		/* 合并link库 */
		LOG.info("merge link database");
		Database linkDatabase = env.openDatabase(null, "link", BerkeleyDBUtils.defaultDBConfig);

		Cursor linkCursor = linkDatabase.openCursor(null, null);

		while (linkCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			if (!(crawldbDatabase.get(null, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS)) {
				crawldbDatabase.put(null, key, value);
			}
		}
		linkCursor.close();
		linkDatabase.close();
		LOG.info("end merge");

		crawldbDatabase.sync();
		crawldbDatabase.close();

		env.removeDatabase(null, "fetch");
		LOG.debug("remove fetch database");
		env.removeDatabase(null, "link");
		LOG.debug("remove link database");

	}

	/**
	 * 父类的实现，打开数据库资源
	 *
	 * @see cn.DBManager.hfut.dmic.webcollector.crawldb.DBManager#open()
	 */
	@Override
	public void open() throws Exception {
		// 创建数据库实例对象存放的物理地址
		File dir = new File(crawlPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 创建内存数据库基础环境，如果不存在则直接创建物理环境。
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setAllowCreate(true);

		env = new Environment(dir, environmentConfig);
	}

	@Override
	public void unlock() throws Exception {
		lockDatabase = env.openDatabase(null, "lock", BerkeleyDBUtils.defaultDBConfig);
		DatabaseEntry key = new DatabaseEntry("lock".getBytes("utf-8"));
		DatabaseEntry value = new DatabaseEntry("unlocked".getBytes("utf-8"));
		lockDatabase.put(null, key, value);
		// 满足条件时候同步内存和磁盘数据
		lockDatabase.sync();
		lockDatabase.close();
	}

	@Override
	public void writeRedirectSegment(CrawlDatum datum, String realUrl) throws Exception {
		BerkeleyDBUtils.put(redirectDatabase, datum.getKey(), realUrl);
		// 满足条件时候同步内存和磁盘数据
		if (count_redirect.incrementAndGet() % BUFFER_SIZE == 0) {
			redirectDatabase.sync();
		}
	}

	@Override
	public void wrtieFetchSegment(CrawlDatum fetchDatum) throws Exception {
		BerkeleyDBUtils.writeDatum(fetchDatabase, fetchDatum);
		// 满足条件时候同步内存和磁盘数据
		if (count_fetch.incrementAndGet() % BUFFER_SIZE == 0) {
			fetchDatabase.sync();
		}
	}

	@Override
	public void wrtieParseSegment(CrawlDatums parseDatums) throws Exception {
		for (CrawlDatum datum : parseDatums) {
			BerkeleyDBUtils.writeDatum(linkDatabase, datum);
		}

		// 满足条件时候同步内存和磁盘数据
		if (count_link.incrementAndGet() % BUFFER_SIZE == 0) {
			linkDatabase.sync();
		}
	}

}
