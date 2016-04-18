package cn.vfire.web.collector3.plugin.redis;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.SegmentWriter;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.lang.enums.CrawlerExpInfo;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlDatums;
import cn.vfire.web.collector3.model.Links;

@Slf4j
public class RedisSegmentWriter implements SegmentWriter {

	private String key;

	public RedisSegmentWriter(String key) {
		this.key = key;
	}

	@Override
	public void initSegmentWriter() throws CrawlerDBException {
		log.debug("Redis数据段操作初始化完成。");
	}

	@Override
	public void wrtieFetchSegment(CrawlDatum fetchDatum) throws CrawlerDBException {

		String value = SerializeUtils.serializeForJson(fetchDatum, CrawlDatum.class);

		Jedis jedis = RedisDBConfig.getJedis();

		try {

			Pipeline pipeline = jedis.pipelined();

			pipeline.lpush(key, value);

			List<Object> results = pipeline.syncAndReturnAll();

			log.debug("Redis数据库段写入操作成功，操作结果{}。", results);

		} catch (Exception e) {

			throw new CrawlerDBException(CrawlerExpInfo.FAIL.setInfo("Redis数据段操作写入失败。"), e);

		} finally {

			jedis.close();

		}

	}

	@Override
	public void deleteFetchSegment(CrawlDatum fetchDatum) throws CrawlerDBException {

	}

	@Override
	public void wrtieParseSegment(CrawlDatums parseDatums) throws CrawlerDBException {

		Jedis jedis = RedisDBConfig.getJedis();

		try {

			Pipeline pipeline = jedis.pipelined();

			for (CrawlDatum fetchDatum : parseDatums) {

				String value = SerializeUtils.serializeForJson(fetchDatum, CrawlDatum.class);

				pipeline.lpush(key, value);

			}

			List<Object> results = pipeline.syncAndReturnAll();

			log.debug("Redis数据库段写入操作成功，操作结果{}。", results);

		} catch (Exception e) {

			throw new CrawlerDBException(CrawlerExpInfo.FAIL.setInfo("Redis数据段操作写入失败。"), e);

		} finally {

			jedis.close();

		}
	}

	@Override
	public void wrtieParseSegment(Links links) throws CrawlerDBException {
		this.wrtieParseSegment(new CrawlDatums(links));
	}

	@Override
	public void closeSegmentWriter() throws CrawlerDBException {
		log.debug("Redis数据段操作关闭。");
	}

}
