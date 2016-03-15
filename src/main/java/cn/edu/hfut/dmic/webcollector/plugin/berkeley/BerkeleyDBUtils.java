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

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.util.CrawlDatumFormater;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import java.io.UnsupportedEncodingException;

/**
 * BerkeleyDB数据库的工具类
 */
public class BerkeleyDBUtils {

	/** BerkeleyDB数据库的默认配置信息 */
	public static DatabaseConfig defaultDBConfig;

	static {
		defaultDBConfig = createDefaultDBConfig();
	}

	/**
	 * 构造默认配置信息。
	 * 
	 * @return
	 */
	public static DatabaseConfig createDefaultDBConfig() {
		DatabaseConfig databaseConfig = new DatabaseConfig();
		// 如果数据库不存在则创建一个新的数据库
		databaseConfig.setAllowCreate(true);
		// 开启数据库延迟写入机制
		databaseConfig.setDeferredWrite(true);
		return databaseConfig;
	}

	/**
	 * 写数据，保存数据
	 * 
	 * @param database
	 * @param datum
	 * @throws Exception
	 */
	public static void writeDatum(Database database, CrawlDatum datum) throws Exception {
		String key = datum.getKey();
		String value = CrawlDatumFormater.datumToJsonStr(datum);
		put(database, key, value);
	}

	/**
	 * 写数据，数据形式为k=v方式。
	 * 
	 * @param database
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void put(Database database, String key, String value) throws Exception {
		database.put(null, strToEntry(key), strToEntry(value));
	}

	/**
	 * 将str对象处理成数据库识别的字节实体，并通过UTF-8编码。
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static DatabaseEntry strToEntry(String str) throws UnsupportedEncodingException {
		return new DatabaseEntry(str.getBytes("utf-8"));
	}

	/**
	 * 创建任务对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static CrawlDatum createCrawlDatum(DatabaseEntry key, DatabaseEntry value) throws Exception {
		String datumKey = new String(key.getData(), "utf-8");
		String valueStr = new String(value.getData(), "utf-8");
		return CrawlDatumFormater.jsonStrToDatum(datumKey, valueStr);
	}
}
