package cn.vfire.web.collector3.plugin.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.crawldb.Generator;
import cn.vfire.web.collector3.crawldb.Injector;
import cn.vfire.web.collector3.crawldb.SegmentWriter;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.tools.Tools;

@Slf4j
public class RedisDBManager extends DBManager implements SegmentWriter, Injector {

	private Generator generator;

	private RedisDBConfig.DBName dbName;

	public RedisDBManager(String dbName) {
		this.dbName = RedisDBConfig.getDBName(dbName);
		this.generator = new RedisGenerator(this.dbName);
		super.injector = new RedisInjector(this.dbName);
		super.segmentWriter = new RedisSegmentWriter(this.dbName);
	}

	@Override
	public void clear() throws CrawlerDBException {
		Jedis jedis = RedisDBConfig.getJedis();
		String rs = jedis.flushDB();
		log.info("清空Redis数据库数据，结果{}。", rs);
	}

	@Override
	public void close() throws CrawlerDBException {
		RedisDBConfig.close();
		log.info("关闭Redis数据库服务，主机信息{}:{}。", RedisDBConfig.HOST, RedisDBConfig.PORT);
	}

	@Override
	public Generator getGenerator() {
		return this.generator;
	}

	@Override
	public boolean isDBExists() {
		boolean flag = Tools.netTelnet(RedisDBConfig.HOST, RedisDBConfig.PORT);
		return flag;
	}

	@Override
	public void open() throws CrawlerDBException {

		CrawlDatum crawlDatum = null;

		String value = null;

		Jedis jedis = RedisDBConfig.getJedis();

		try {

			while ((value = jedis.rpop(this.dbName.queueInvalidKey)) != null) {

				crawlDatum = SerializeUtils.deserializeFromJson(value, CrawlDatum.class);

				value = SerializeUtils.serializeForJson(crawlDatum.init(), CrawlDatum.class);

				jedis.lpush(this.dbName.queueKey, value);

			}

			log.debug("Redis数据段操作初始化完成。");

		} catch (Exception e) {

			throw new CrawlerDBException(CrawlerExpInfo.FAIL.setInfo("Redis数据段操作初始化失败。"), e);

		} finally {

			jedis.close();

		}

		log.info("开启Redis数据库服务，主机信息{}:{}。", RedisDBConfig.HOST, RedisDBConfig.PORT);
	}

}
