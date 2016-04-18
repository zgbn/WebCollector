package cn.vfire.web.collector3.plugin.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
public class RedisDBConfig {

	/** Redis服务器IP */
	public static final String HOST = "121.42.10.67";

	/** Redis的端口号 */
	public static final int PORT = 6379;

	/** 访问密码 */
	private static final String AUTH = "zheshiyigemima";

	/** 超时时间 */
	public static final int TIMEOUT = 60000;

	/**
	 * 可用连接实例的最大数目，默认值为8；
	 * 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	 */
	private static final int MAX_ACTIVE = 8;

	/** 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。 */
	private static final int MAX_IDLE = 8;

	/** 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException； */
	private static final int MAX_WAIT = -1;

	/** 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的； */
	private static final boolean TEST_ON_BORROW = true;

	/** redis过期时间,以秒为单位 */
	public final static int EXRP_HOUR = 60 * 60; // 一小时
	/** redis过期时间,以秒为单位 */
	public final static int EXRP_DAY = 60 * 60 * 24; // 一天
	/** redis过期时间,以秒为单位 */
	public final static int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月
	
	private static JedisPool JedisPool = null;

	/**
	 * 初始化Redis连接池
	 */
	private static void initialPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(MAX_ACTIVE);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWaitMillis(MAX_WAIT);
		config.setTestOnBorrow(TEST_ON_BORROW);
		JedisPool = new JedisPool(config, HOST, PORT, TIMEOUT, AUTH);
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (JedisPool == null) {
			initialPool();
		}
	}

	/**
	 * 同步获取Jedis实例
	 * 
	 * @return Jedis
	 */
	public synchronized static Jedis getJedis() {
		if (JedisPool == null) {
			poolInit();
		}

		Jedis jedis = null;

		try {
			if (JedisPool != null) {
				jedis = JedisPool.getResource();
			}
		} catch (Exception e) {
			if (jedis != null) {
				jedis.close();
			}
			log.error("获取Redist连接对象发生异常", e);
		}

		return jedis;
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public static void close() {
		if (JedisPool != null) {
			JedisPool.close();
		}
	}

}
