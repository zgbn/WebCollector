package cn.vfire.web.collector3.plugin.berkeley;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.SegmentWriter;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlDatums;
import cn.vfire.web.collector3.model.Links;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;

@Slf4j
public class BerkeleySegmentWriter implements SegmentWriter {

	private Environment environment;

	private String crawlerName;

	private AtomicInteger count_crawl;

	private Database crawlDatabase;

	private final int buffer_size = 20;

	public BerkeleySegmentWriter(Environment environment, String crawlerName) {
		this.environment = environment;
		this.crawlerName = crawlerName;
	}

	@Override
	public void closeSegmentWriter() throws CrawlerDBException {
		this.crawlDatabase.sync();
		this.crawlDatabase.close();
	}

	@Override
	public void initSegmentWriter() throws CrawlerDBException {
		this.crawlDatabase = this.environment.openDatabase(null, this.crawlerName, BerkeleyDBConfig.DefaultDBConfig);
		this.count_crawl = new AtomicInteger(0);
	}

	@Override
	public void wrtieFetchSegment(CrawlDatum crawlDatum) throws CrawlerDBException {

		DatabaseEntry dbkey = BerkeleyDBConfig.toDatabaseEntry(crawlDatum.getKey());

		DatabaseEntry dbvalue = BerkeleyDBConfig.toDatabaseEntry(SerializeUtils.serializeForJson(crawlDatum, CrawlDatum.class));

		this.crawlDatabase.put(null, dbkey, dbvalue);

		log.debug("保存任务{}成功。", crawlDatum.getKey());

		if (this.count_crawl.incrementAndGet() % buffer_size == 0) {
			this.crawlDatabase.sync();
		}

	}

	@Override
	public void wrtieParseSegment(CrawlDatums parseDatums) throws CrawlerDBException {

		for (CrawlDatum crawlDatum : parseDatums) {
			this.wrtieFetchSegment(crawlDatum);
		}

		this.crawlDatabase.sync();

	}

	@Override
	public void deleteFetchSegment(CrawlDatum crawlDatum) throws CrawlerDBException {

		DatabaseEntry dbkey = BerkeleyDBConfig.toDatabaseEntry(crawlDatum.getKey());

		this.crawlDatabase.delete(null, dbkey);

		log.debug("删除任务{}成功。", crawlDatum.getKey());

		if (this.count_crawl.incrementAndGet() % buffer_size == 0) {
			this.crawlDatabase.sync();
		}

	}

	@Override
	public void wrtieParseSegment(Links links) throws CrawlerDBException {
		this.wrtieParseSegment(new CrawlDatums(links));
	}
}
