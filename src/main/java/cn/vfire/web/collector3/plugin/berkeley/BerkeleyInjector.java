package cn.vfire.web.collector3.plugin.berkeley;

import lombok.extern.slf4j.Slf4j;
import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.Injector;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;

@Slf4j
public class BerkeleyInjector implements Injector {

	private Environment environment;

	private String crawlerName;

	public BerkeleyInjector(Environment environment, String crawlerName) {
		this.environment = environment;
		this.crawlerName = crawlerName;
	}

	@Override
	public void inject(CrawlDatum crawlDatum) throws CrawlerDBException {

		Database database = this.environment.openDatabase(null, this.crawlerName, BerkeleyDBConfig.DefaultDBConfig);

		DatabaseEntry dbkey = BerkeleyDBConfig.toDatabaseEntry(crawlDatum.getKey());

		DatabaseEntry dbvalue = BerkeleyDBConfig.toDatabaseEntry(SerializeUtils.serializeForJson(crawlDatum, CrawlDatum.class));

		// 保存数据
		database.put(null, dbkey, dbvalue);

		// 同步内存与硬盘数据
		database.sync();

		// 关闭数据库连接
		database.close();

		log.debug("保存任务{}成功。", crawlDatum.getKey());

	}

}
