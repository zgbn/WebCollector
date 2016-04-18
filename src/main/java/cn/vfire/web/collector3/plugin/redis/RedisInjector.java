package cn.vfire.web.collector3.plugin.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.Injector;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;

@Slf4j
public class RedisInjector implements Injector {

	private RedisDBConfig.DBName dbName;

	public RedisInjector(RedisDBConfig.DBName dbName) {
		this.dbName = dbName;
	}

	@Override
	public void inject(CrawlDatum datum) throws CrawlerDBException {

		boolean invalid = datum.isInvalid();

		String value = SerializeUtils.serializeForJson(datum, CrawlDatum.class);

		Jedis jedis = RedisDBConfig.getJedis();

		try {

			if (invalid) {
				jedis.lpush(this.dbName.queueInvalidKey, value);
			} else {
				jedis.lpush(this.dbName.queueKey, value);
			}

			log.debug("保存任务{}成功。", datum.getKey());

		} catch (Exception e) {

			log.warn("Redis数据库保存CrawlDatum失败。", e);

		} finally {

			if (jedis != null) {

				jedis.close();

			}
		}
	}
}
