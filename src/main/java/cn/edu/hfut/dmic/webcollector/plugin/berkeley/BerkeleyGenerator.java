/*
 * Copyright (C) 2014 hu
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
package cn.edu.hfut.dmic.webcollector.plugin.berkeley;

import cn.edu.hfut.dmic.webcollector.crawldb.Generator;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.util.Config;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Berkeley数据库模式的任务生成器
 */
public class BerkeleyGenerator implements Generator {

	public static final Logger LOG = LoggerFactory.getLogger(BerkeleyGenerator.class);

	/** Berkeley数据库游标 */
	Cursor cursor = null;
	/** Berkeley数据库句柄。 */
	Database crawldbDatabase = null;
	/** Berkeley数据库环境。 */
	Environment env = null;
	/** 任务生成器生成任务的计数器数 */
	protected int totalGenerate = 0;
	/** 任务执行个数 */
	protected int topN = -1;
	/** 最大执行数 */
	protected int maxExecuteCount = Config.MAX_EXECUTE_COUNT;

	/** crawlPath数据库路径 */
	String crawlPath;

	/** 将数据库密钥和数据项编码为字节数组。 */
	public DatabaseEntry key = new DatabaseEntry();

	/** 将数据库密钥和数据项编码为字节数组。 */
	public DatabaseEntry value = new DatabaseEntry();

	public BerkeleyGenerator(String crawlPath) {
		this.crawlPath = crawlPath;
	}

	/**
	 * 关闭任务生成器
	 */
	@Override
	public void close() throws Exception {
		// 关闭数据库游标
		if (cursor != null) {
			cursor.close();
		}
		// 关闭数据库
		cursor = null;
		if (crawldbDatabase != null) {
			crawldbDatabase.close();
		}
		// 关闭数据库环境应用
		if (env != null) {
			env.close();
		}
	}

	public int getMaxExecuteCount() {
		return maxExecuteCount;
	}

	public int getTopN() {
		return topN;
	}

	@Override
	public int getTotalGenerate() {
		return totalGenerate;
	}

	@Override
	public CrawlDatum next() {
		if (topN >= 0) {
			if (totalGenerate >= topN) {
				// 当指定的top任务个数，则生成任务计数器到达指定top个数时返回null，不再返回下一个任务。
				return null;
			}
		}

		if (cursor == null) {
			// 当数据库游标为null的时候，需要重新创建数据库
			crawldbDatabase = env.openDatabase(null, "crawldb", BerkeleyDBUtils.defaultDBConfig);
			// 通过数据库实体获取游标对象
			cursor = crawldbDatabase.openCursor(null, CursorConfig.DEFAULT);
		}

		while (true) {
			// 通过游标从数据库提取任务。
			if (cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

				// 提取任务成功

				try {
					// 更具key value创建任务对象
					CrawlDatum datum = BerkeleyDBUtils.createCrawlDatum(key, value);
					if (datum.getStatus() == CrawlDatum.STATUS_DB_SUCCESS) {
						// 该任务执行成功跳过
						continue;
					} else {
						// 该任务执行失败
						if (datum.getExecuteCount() > maxExecuteCount) {
							// 该任务执行次数超过指定的最大执行次数时跳过
							continue;
						}
						// 该任务执行失败，总任务计数器+1
						totalGenerate++;
						// 返回该任务，下次继续执行
						return datum;
					}
				} catch (Exception ex) {
					LOG.info("Exception when generating", ex);
					continue;
				}
			} else {
				// 提取任务失败
				return null;
			}
		}
	}

	@Override
	public void open() throws Exception {
		// 创建存放数据库结构目录
		File dir = new File(crawlPath);
		// 创建数据库环境全局配置信息
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		// 如果数据库不存在则创建一个新的数据库
		environmentConfig.setAllowCreate(true);
		// 创建数据库全局环境
		env = new Environment(dir, environmentConfig);
		// 生成任务计数器初始化
		totalGenerate = 0;

	}

	@Override
	public void setMaxExecuteCount(int maxExecuteCount) {
		this.maxExecuteCount = maxExecuteCount;
	}

	@Override
	public void setTopN(int topN) {
		this.topN = topN;
	}
}
