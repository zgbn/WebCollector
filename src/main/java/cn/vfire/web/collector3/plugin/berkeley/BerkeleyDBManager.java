package cn.vfire.web.collector3.plugin.berkeley;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;

import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.crawldb.Generator;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.lang.CrawlerRuntimeException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlDatums;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.crawler.element.DataMode;
import cn.vfire.web.collector3.tools.crawler.format.FormatData;
import cn.vfire.web.collector3.tools.crawler.proxyip.ProxyIpPool;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;

@Slf4j
public class BerkeleyDBManager extends DBManager implements CrawlerInfoWare {

	private String crawlPath;

	private Generator generator;

	private Environment environment;

	private Database crawlDatabase;

	private AtomicInteger count_crawl;

	private String crawlerName;

	private final int buffer_size = 50;

	/**
	 * 根据给定数据库物理地址创建数据库服务
	 * 
	 * @param crawlPath
	 *            数据库物理路径
	 */
	public BerkeleyDBManager(String crawlPath) {
		this.crawlPath = crawlPath;
	}

	public BerkeleyDBManager() {
		this.crawlPath = BerkeleyDBConfig.DefaultHomePath;
	}

	/**
	 * 清空数据库
	 */
	@Override
	public void clear() throws CrawlerDBException {
		File dir = new File(this.crawlPath);
		try {
			FileUtils.deleteDirectory(dir);
			log.info("清除BerkeleyDB数据库服务，物理文件{}。", this.crawlPath);
		} catch (IOException e) {
			throw new CrawlerDBException(CrawlerExpInfo.FAIL.setInfo("清除{}数据库服务。", this.crawlPath), e);
		}
	}

	/**
	 * 关闭数据库
	 */
	@Override
	public void close() throws CrawlerDBException {
		this.environment.sync();
		this.environment.close();
		log.info("关闭BerkeleyDB数据库服务。路径{}，实例名称{}。", this.crawlPath, this.crawlerName);
	}

	/**
	 * 关闭段写入
	 * 
	 * @throws CrawlerDBException
	 */
	@Override
	public void closeSegmentWriter() throws CrawlerDBException {
		this.crawlDatabase.sync();
		this.crawlDatabase.close();
		log.info("关闭段操作数据库实例{}。", this.crawlDatabase);
	}

	/**
	 * 获取任务分配者
	 */
	@Override
	public Generator getGenerator() {
		if (this.generator == null) {
			this.generator = new BerkeleyGenerator(this.crawlPath, this.crawlerName);
		}
		return this.generator;
	}

	/**
	 * 初始化段写
	 * 
	 * @throws Exception
	 */
	@Override
	public void initSegmentWriter() throws CrawlerDBException {
		this.crawlDatabase = this.environment.openDatabase(null, this.crawlerName, BerkeleyDBConfig.DefaultDBConfig);
		this.count_crawl = new AtomicInteger(0);
		log.info("初始化段操作数据库实例{}。", this.crawlDatabase);
	}

	@Override
	public void inject(CrawlDatum crawlDatum) throws CrawlerDBException {

		if (this.environment == null) {
			File dir = new File(this.crawlPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			this.environment = new Environment(dir, BerkeleyDBConfig.DefaultEnvConfig);
		}

		Database database = this.environment.openDatabase(null, this.crawlerName, BerkeleyDBConfig.DefaultDBConfig);

		DatabaseEntry dbkey = BerkeleyDBConfig.toDatabaseEntry(crawlDatum.getKey());

		DatabaseEntry dbvalue = BerkeleyDBConfig.toDatabaseEntry(SerializeUtils.serializeForJson(crawlDatum, CrawlDatum.class));

		// 保存数据
		database.put(null, dbkey, dbvalue);

		// 同步内存与硬盘数据
		database.sync();

		// 关闭数据库连接
		database.close();

		this.environment.sync();

		this.environment.close();

		log.debug("保存任务{}成功。", crawlDatum.getKey());

	}

	/**
	 * 判断数据库服务是否存在
	 */
	@Override
	public boolean isDBExists() {
		File dir = new File(this.crawlPath);
		boolean flag = dir.exists();
		log.info("检测BerkeleyDB数据库服务{}，物理文件{}。", flag ? "存在" : "不存在", dir.getPath());
		return dir.exists();
	}

	/**
	 * 开启数据库服务
	 */
	@Override
	public void open() throws CrawlerDBException {

		this.initValidator();

		// 创建数据库实例对象存放的物理地址
		File dir = new File(this.crawlPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 创建内存数据库基础环境，如果不存在则直接创建物理环境。
		this.environment = new Environment(dir, BerkeleyDBConfig.DefaultEnvConfig);

		log.info("开启BerkeleyDB数据库服务。路径{}，实例名称{}。", this.crawlPath, this.crawlerName);

	}

	private void initValidator() {
		if (this.crawlerName == null || "".equals(this.crawlerName)) {
			throw new CrawlerRuntimeException(CrawlerExpInfo.VALIDATE.setInfo("属性crawlerName不能为{}", this.crawlerName));
		}
	}

	/**
	 * 设置爬虫基本信息
	 */
	@Override
	public void setCrawlerAttrInfo(CrawlerAttrInfo crawlerAttrInfo) {
	}

	/**
	 * 设置爬虫名字
	 */
	@Override
	public void setName(String name) {
		this.crawlerName = name;
	}

	/**
	 * 段写入任务
	 */
	@Override
	public void wrtieFetchSegment(CrawlDatum crawlDatum) throws CrawlerDBException {

		DatabaseEntry dbkey = BerkeleyDBConfig.toDatabaseEntry(crawlDatum.getKey());

		DatabaseEntry dbvalue = BerkeleyDBConfig.toDatabaseEntry(SerializeUtils.serializeForJson(crawlDatum, CrawlDatum.class));

		try {
			this.crawlDatabase.put(null, dbkey, dbvalue);

			log.debug("保存任务{}成功。", crawlDatum.getKey());

			if (this.count_crawl.incrementAndGet() % buffer_size == 0) {
				this.crawlDatabase.sync();
			}
		} catch (Exception e) {
			throw new CrawlerDBException(e);
		}

	}

	/**
	 * 段写入任务
	 */
	@Override
	public void wrtieParseSegment(CrawlDatums parseDatums) throws CrawlerDBException {

		for (CrawlDatum crawlDatum : parseDatums) {
			this.wrtieFetchSegment(crawlDatum);
		}

		this.crawlDatabase.sync();

	}

	@Override
	public void setDataModes(List<DataMode> dataModes) {
	}

	@Override
	public void setRegexRules(List<String> regex) {
	}

	@Override
	public void setUnregexRules(List<String> regex) {
	}

	@Override
	public void deleteFetchSegment(CrawlDatum crawlDatum) throws CrawlerDBException {

		DatabaseEntry dbkey = BerkeleyDBConfig.toDatabaseEntry(crawlDatum.getKey());

		this.crawlDatabase.delete(null, dbkey);

		log.debug("移除任务{}成功。", crawlDatum.getKey());

		if (this.count_crawl.incrementAndGet() % buffer_size == 0) {
			this.crawlDatabase.sync();
		}

	}

	@Override
	public void setProxyIpPool(ProxyIpPool proxyIpPool) {

	}

	@Override
	public void setFormatData(FormatData formatData) {

	}

	@Override
	public void setSeedurl(String seedUrl) {

	}

	@Override
	public void setSnapshot(int time, int size) {
		// TODO Auto-generated method stub
	}

}
