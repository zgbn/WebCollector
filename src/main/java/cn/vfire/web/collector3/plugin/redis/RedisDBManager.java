package cn.vfire.web.collector3.plugin.redis;

import redis.clients.jedis.Jedis;
import lombok.extern.slf4j.Slf4j;
import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.crawldb.Generator;
import cn.vfire.web.collector3.crawldb.Injector;
import cn.vfire.web.collector3.crawldb.SegmentWriter;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.tools.Tools;

@Slf4j
public class RedisDBManager extends DBManager implements SegmentWriter, Injector {

	private String queueName = "Queue";

	private Generator generator;

	public RedisDBManager(String dbName) {
		this.queueName = String.format("%s%s", dbName, "Queue");
		this.generator = new RedisGenerator(this.queueName);
		super.injector = new RedisInjector(this.queueName);
		super.segmentWriter = new RedisSegmentWriter(this.queueName);
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
		log.info("开启Redis数据库服务，主机信息{}:{}。", RedisDBConfig.HOST, RedisDBConfig.PORT);
	}

}
