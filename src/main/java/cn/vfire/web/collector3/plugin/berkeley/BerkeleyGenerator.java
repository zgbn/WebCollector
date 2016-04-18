package cn.vfire.web.collector3.plugin.berkeley;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.Generator;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

@Slf4j
public class BerkeleyGenerator implements Generator {

	private String crawlPath;

	private String crawlName;

	private Cursor cursor;

	private Database crawldbDatabase;

	/** 将数据库密钥和数据项编码为字节数组。 */
	private DatabaseEntry dbkey = new DatabaseEntry();

	/** 将数据库密钥和数据项编码为字节数组。 */
	private DatabaseEntry dbvalue = new DatabaseEntry();

	private AtomicInteger totalGenerate = new AtomicInteger(0);

	private Environment environment;

	public BerkeleyGenerator(String crawlPath, String crawlName) {
		this.crawlPath = crawlPath;
		this.crawlName = crawlName;
	}

	@Override
	public void close() throws CrawlerDBException {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (this.cursor != null) {
			cursor.close();
			cursor = null;
		}
		// 关闭数据库
		if (crawldbDatabase != null) {
			crawldbDatabase.close();
		}
		// 关闭数据库环境应用
		if (this.environment != null) {
			this.environment.close();
		}

	}

	@Override
	public int getTotalGenerate() {
		return this.totalGenerate.get();
	}

	private boolean isSkip(CrawlDatum crawlDatum) {
		return crawlDatum.isInvalid();
	}

	@Override
	public CrawlDatum next() {

		log.debug("BerkeleyGenerator尝试读取下一个任务数据。");

		if (this.cursor == null) {
			// 当数据库游标为null的时候，需要重新创建数据库
			this.crawldbDatabase = this.environment.openDatabase(null, this.crawlName, BerkeleyDBConfig.DefaultDBConfig);
			// 通过数据库实体获取游标对象
			this.cursor = this.crawldbDatabase.openCursor(null, CursorConfig.DEFAULT);
		}

		while (true) {

			// 通过游标从数据库提取任务。
			if (cursor.getNext(dbkey, dbvalue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

				CrawlDatum crawlDatum = SerializeUtils.deserializeFromJson(BerkeleyDBConfig.fromDatabaseEntry(dbvalue), CrawlDatum.class);

				if (this.isSkip(crawlDatum)) {
					log.debug("BerkeleyGenerator读取到无效的任务则跳过。");
					continue;
				}

				this.totalGenerate.incrementAndGet();

				return crawlDatum;

			} else {
				// 提取任务失败
				return null;
			}
		}
	}

	@Override
	public void open() throws CrawlerDBException {

		// 创建数据库实例对象存放的物理地址
		File dir = new File(this.crawlPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 创建内存数据库基础环境，如果不存在则直接创建物理环境。
		this.environment = new Environment(dir, BerkeleyDBConfig.DefaultEnvConfig);
	}

}
