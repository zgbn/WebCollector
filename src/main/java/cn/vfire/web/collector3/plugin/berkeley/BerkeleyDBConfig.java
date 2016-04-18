package cn.vfire.web.collector3.plugin.berkeley;

import java.io.File;
import java.util.Comparator;

import cn.vfire.web.collector3.lang.CrawlerRuntimeException;

import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.EnvironmentConfig;

public class BerkeleyDBConfig {

	/** BerkeleyDB数据库的默认配置信息 */
	public static DatabaseConfig DefaultDBConfig;

	public static EnvironmentConfig DefaultEnvConfig;

	public static String DefaultHomePath;

	static {
		DefaultDBConfig = createDefaultDBConfig();
		DefaultEnvConfig = createDefaultEnvConfig();
		DefaultHomePath = createDefaultHomePath();
	}

	private static String createDefaultHomePath() {
		String path = System.getProperty("user.home", "/");
		String dir = new File(System.getProperty("user.dir", "BerkeleyDB")).getName();
		String homePath = String.format("%s/%s-tmp", path, dir);
		File homePathDir = new File(homePath);
		if (homePathDir.exists() == false) {
			homePathDir.mkdirs();
		}
		return homePathDir.getPath();
	}

	/**
	 * 构造默认配置信息。
	 * 
	 * @return
	 */
	private static EnvironmentConfig createDefaultEnvConfig() {
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setAllowCreate(true);
		return environmentConfig;
	}

	public static class SortComparator implements Comparator<byte[]> {
		@Override
		public int compare(byte[] o1, byte[] o2) {

			String s1 = fromDatabaseEntry(new DatabaseEntry(o1));

			String s2 = fromDatabaseEntry(new DatabaseEntry(o2));

			return s1.compareTo(s2);
		}
	}

	private static DatabaseConfig createDefaultDBConfig() {

		DatabaseConfig databaseConfig = new DatabaseConfig();
		// 如果数据库不存在则创建一个新的数据库
		databaseConfig.setAllowCreate(true);
		// 开启数据库延迟写入机制
		databaseConfig.setDeferredWrite(true);
		// 设置一个key是否允许存储多个值，true代表允许，默认false.
		databaseConfig.setSortedDuplicates(false);
		// 设置用于B tree比较的比较器，通常是用来排序
		databaseConfig.setBtreeComparator(SortComparator.class);

		return databaseConfig;
	}

	public static DatabaseEntry toDatabaseEntry() {
		return new DatabaseEntry();
	}

	public static DatabaseEntry toDatabaseEntry(String entry) {
		try {
			if (entry == null) {
				return new DatabaseEntry();
			} else {
				return new DatabaseEntry(entry.getBytes("utf-8"));
			}
		} catch (Exception e) {
			throw new CrawlerRuntimeException(e);
		}
	}

	/**
	 * 将DatabaseEntry对象转换为String
	 * 
	 * @param databaseEntry
	 * @return
	 */
	public static String fromDatabaseEntry(DatabaseEntry databaseEntry) {
		try {
			String rs = new String(databaseEntry.getData(), "utf-8");
			return rs;
		} catch (Exception e) {
			throw new CrawlerRuntimeException(e);
		}
	}
}
