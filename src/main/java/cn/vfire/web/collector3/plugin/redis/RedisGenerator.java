package cn.vfire.web.collector3.plugin.redis;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.Generator;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;

@Slf4j
public class RedisGenerator implements Generator {

	private RedisDBConfig.DBName dbName;

	private AtomicInteger totalGenerate = new AtomicInteger(0);

	public RedisGenerator(RedisDBConfig.DBName dbName) {
		this.dbName = dbName;
	}

	@Override
	public CrawlDatum next() {

		CrawlDatum crawlDatum = null;

		Jedis jedis = RedisDBConfig.getJedis();

		String value = jedis.rpop(this.dbName.queueKey);

		try {
			if (value != null) {

				crawlDatum = SerializeUtils.deserializeFromJson(value, CrawlDatum.class);

				totalGenerate.incrementAndGet();

			}
		} catch (Exception e) {

			log.warn("Redis数据库弹出数据失败。", e);

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return crawlDatum;
	}

	@Override
	public void open() throws CrawlerDBException {
		log.info("Redis数据库弹出操作初始化完成。");
	}

	@Override
	public int getTotalGenerate() {
		return this.totalGenerate.get();
	}

	@Override
	public void close() throws CrawlerDBException {
		log.info("Redis数据库弹出操作关闭。");
	}

}
