package cn.vfire.web.collector3.crawler.plugin.berkeley;

import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.EnvironmentConfig;

public class BerkeleyDBConfig {

	/** BerkeleyDB数据库的默认配置信息 */
	public static DatabaseConfig DefaultDBConfig;

	public static EnvironmentConfig DefaultEnvConfig;


	static {
		DefaultDBConfig = createDefaultDBConfig();
		DefaultEnvConfig = createDefaultEnvConfig();
	}


	/**
	 * 构造默认配置信息。
	 * 
	 * @return
	 */
	public static EnvironmentConfig createDefaultEnvConfig() {
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setAllowCreate(true);
		return environmentConfig;
	}


	public static DatabaseConfig createDefaultDBConfig() {
		DatabaseConfig databaseConfig = new DatabaseConfig();
		// 如果数据库不存在则创建一个新的数据库
		databaseConfig.setAllowCreate(true);
		// 开启数据库延迟写入机制
		databaseConfig.setDeferredWrite(true);
		return databaseConfig;
	}
}
