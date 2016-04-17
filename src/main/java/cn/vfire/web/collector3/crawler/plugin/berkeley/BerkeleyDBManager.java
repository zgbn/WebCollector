package cn.vfire.web.collector3.crawler.plugin.berkeley;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.sleepycat.je.Environment;

import cn.vfire.web.collector3.db.AbsDBManager;
import cn.vfire.web.collector3.db.Statement;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BerkeleyDBManager extends AbsDBManager {

	private static final String name = "BerkeleyDB";

	// 数据库环境
	@Getter
	private Environment environment;

	// 数据库文件名
	private String crawlPath;


	/**
	 * 创建数据库服务，指定数据库文件存放的物理地址。
	 * 
	 * @param crawlPath
	 */
	public BerkeleyDBManager(String crawlPath) {
		this.crawlPath = crawlPath;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.vfire.web.collector3.db.DBManager#isExist()
	 */
	@Override
	public boolean isExist() {
		File dir = new File(crawlPath);
		return dir.exists();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.vfire.web.collector3.db.DBManager#clean()
	 */
	@Override
	public void clean() throws CrawlerDBException {

		this.close();

		File dir = new File(this.crawlPath);

		try {
			FileUtils.deleteDirectory(dir);
		}
		catch (IOException e) {
			throw new CrawlerDBException(CrawlerExpInfo.FAIL.setInfo("清除{}数据库服务。", this.getProductName()), e);
		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.vfire.web.collector3.db.DBManager#open()
	 */
	@Override
	public void open() throws CrawlerDBException {
		// 创建数据库实例对象存放的物理地址
		File dir = new File(this.crawlPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 创建内存数据库基础环境，如果不存在则直接创建物理环境。
		this.environment = new Environment(dir, BerkeleyDBConfig.DefaultEnvConfig);

		log.info("开启{}数据库服务。", this.getProductName());

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.vfire.web.collector3.db.DBManager#close()
	 */
	@Override
	public void close() throws CrawlerDBException {

		if (this.environment != null) {
			this.environment.sync();
			this.environment.close();
		}

		log.info("关闭{}数据库服务。", this.getProductName());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.vfire.web.collector3.db.DBManager#getProductName()
	 */
	@Override
	public String getProductName() {
		return name;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.vfire.web.collector3.db.DBManager#createStatement(java.lang.String)
	 */
	@Override
	public Statement createStatement(String name) {

		BerkeleyDBStatement statement = new BerkeleyDBStatement(name, this.environment);

		return statement;
	}

}
