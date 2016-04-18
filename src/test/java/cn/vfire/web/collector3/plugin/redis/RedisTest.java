package cn.vfire.web.collector3.plugin.redis;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

public class RedisTest {

	/**
	 * 在不同的线程中使用相同的Jedis实例会发生奇怪的错误。但是创建太多的实现也不好因为这意味着会建立很多sokcet连接，
	 * 也会导致奇怪的错误发生。单一Jedis实例不是线程安全的。为了避免这些问题，可以使用JedisPool,
	 * JedisPool是一个线程安全的网络连接池。可以用JedisPool创建一些可靠Jedis实例，可以从池中拿到Jedis的实例。
	 * 这种方式可以解决那些问题并且会实现高效的性能
	 */

	public static void main(String[] args) {
	}

	/**
	 * 每次set之后都可以返回结果，标记是否成功。
	 */
	@Test
	public void jedisNormal() {

		JedisShardInfo info = new JedisShardInfo("121.42.10.67", 6379);
		info.setPassword("zheshiyigemima");
		Jedis jedis = new Jedis(info);

		long start = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++) {
			String result = jedis.set("n" + i, "n" + i);
		}
		long end = System.currentTimeMillis();
		System.out.println("Simple SET: " + ((end - start) / 1000.0) + " seconds");
		jedis.disconnect();
	}

	/**
	 * 事务方式(Transactions)<br />
	 * 所谓事务，即一个连续操作，是否执行是一个事务，要么完成，要么失败，没有中间状态。<br />
	 * 而redis的事务很简单，他主要目的是保障，一个client发起的事务中的命令可以连续的执行，而中间不会插入其他client的命令，
	 * 也就是事务的连贯性。<br />
	 * 我们调用jedis.watch(…)方法来监控key，如果调用后key值发生变化，则整个事务会执行失败。另外，事务中某个操作失败，
	 * 并不会回滚其他操作。这一点需要注意。还有，我们可以使用discard()方法来取消事务。
	 */
	@Test
	public void jedisTrans() {

		JedisShardInfo info = new JedisShardInfo("121.42.10.67", 6379);
		info.setPassword("zheshiyigemima");
		Jedis jedis = new Jedis(info);

		long start = System.currentTimeMillis();
		Transaction tx = jedis.multi();
		for (int i = 0; i < SIZE; i++) {
			Response<String> res = tx.set("t" + i, "t" + i);
		}
		List<Object> results = tx.exec();
		long end = System.currentTimeMillis();
		System.out.println("Transaction SET: " + ((end - start) / 1000.0) + " seconds");
		jedis.disconnect();
	}

	private static int SIZE = 100;

	/**
	 * (3)管道(Pipelining)<br />
	 * 管道是一种两个进程之间单向通信的机制。<br />
	 * 那再redis中，为何要使用管道呢？<br />
	 * 有时候，我们需要采用异步的方式，一次发送多个指令，并且，不同步等待其返回结果。这样可以取得非常好的执行效率。<br />
	 */
	@Test
	public void jedisPipelined() {

		JedisShardInfo info = new JedisShardInfo("121.42.10.67", 6379);
		info.setPassword("zheshiyigemima");
		Jedis jedis = new Jedis(info);

		Pipeline pipeline = jedis.pipelined();
		long start = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++) {
//			pipeline.set("p" + i, "p" + i);
			Response<String> rs = pipeline.get("p" + i);
			System.out.println(rs);
		}
		List<Object> results = pipeline.syncAndReturnAll();
		long end = System.currentTimeMillis();
		System.out.println("Pipelined SET: " + ((end - start) / 1000.0) + " seconds. results size " + results.size());
		jedis.disconnect();
	}

	/**
	 * (4)管道中调用事务<br />
	 * 对于，事务以及管道，这两个概念我们都清楚了。<br />
	 * 在某种需求下，我们需要异步执行命令，但是，又希望多个命令是有连续的，所以，我们就采用管道加事务的调用方式。<br />
	 * jedis是支持在管道中调用事务的。 <br />
	 */
	@Test
	public void jedisCombPipelineTrans() {

		JedisShardInfo info = new JedisShardInfo("121.42.10.67", 6379);
		info.setPassword("zheshiyigemima");
		Jedis jedis = new Jedis(info);

		long start = System.currentTimeMillis();
		Pipeline pipeline = jedis.pipelined();
		pipeline.multi();
		for (int i = 0; i < SIZE; i++) {
			pipeline.set("" + i, "" + i);
		}
		pipeline.exec();
		List<Object> results = pipeline.syncAndReturnAll();
		long end = System.currentTimeMillis();
		System.out.println("Pipelined transaction: " + ((end - start) / 1000.0) + " seconds");
		jedis.disconnect();
	}

	/**
	 * (5)分布式直连同步调用 <br />
	 * 这个是分布式直接连接，并且是同步调用，每步执行都返回执行结果。类似地，还有异步管道调用。 <br />
	 * 其实就是分片。 <br />
	 */
	@Test
	public void jedisShardNormal() {

		JedisShardInfo info1 = new JedisShardInfo("121.42.10.67", 6379);
		info1.setPassword("zheshiyigemima");

		JedisShardInfo info2 = new JedisShardInfo("121.42.10.67", 6379);
		info2.setPassword("zheshiyigemima");

		List<JedisShardInfo> shards = Arrays.asList(info1, info2);

		ShardedJedis sharding = new ShardedJedis(shards);

		long start = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++) {
			String result = sharding.set("sn" + i, "n" + i);
		}
		long end = System.currentTimeMillis();
		System.out.println("Simple@Sharing SET: " + ((end - start) / 1000.0) + " seconds");

		sharding.disconnect();
	}

	/**
	 * (6)分布式直连异步调用
	 */
	@Test
	public void jedisShardpipelined() {

		JedisShardInfo info1 = new JedisShardInfo("121.42.10.67", 6379);
		info1.setPassword("zheshiyigemima");

		JedisShardInfo info2 = new JedisShardInfo("121.42.10.67", 6379);
		info2.setPassword("zheshiyigemima");

		List<JedisShardInfo> shards = Arrays.asList(info1, info2);

		ShardedJedis sharding = new ShardedJedis(shards);

		ShardedJedisPipeline pipeline = sharding.pipelined();
		long start = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++) {
			pipeline.set("sp" + i, "p" + i);
		}
		List<Object> results = pipeline.syncAndReturnAll();
		long end = System.currentTimeMillis();
		System.out.println("Pipelined@Sharing SET: " + ((end - start) / 1000.0) + " seconds");

		sharding.disconnect();
	}

	/**
	 * (7)分布式连接池同步调用 <br />
	 * 如果，你的分布式调用代码是运行在线程中，那么上面两个直连调用方式就不合适了，因为直连方式是非线程安全的，这个时候，你就必须选择连接池调用。 <br />
	 * 连接池的调用方式，适合大规模的redis集群，并且多客户端的操作。 <br />
	 */
	@Test
	public void jedisShardSimplePool() {

		JedisShardInfo info1 = new JedisShardInfo("121.42.10.67", 6379);
		info1.setPassword("zheshiyigemima");

		JedisShardInfo info2 = new JedisShardInfo("121.42.10.67", 6379);
		info2.setPassword("zheshiyigemima");

		List<JedisShardInfo> shards = Arrays.asList(info1, info2);

		ShardedJedisPool pool = new ShardedJedisPool(new JedisPoolConfig(), shards);

		ShardedJedis one = pool.getResource();

		long start = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++) {
			String result = one.set("spn" + i, "n" + i);
		}
		long end = System.currentTimeMillis();
		pool.returnResource(one);
		System.out.println("Simple@Pool SET: " + ((end - start) / 1000.0) + " seconds");

		pool.destroy();
	}

	/**
	 * (8)分布式连接池异步调用
	 */
	@Test
	public void jedisShardPipelinedPool() {

		try {
			JedisShardInfo info1 = new JedisShardInfo("121.42.10.67", 6379);
			info1.setPassword("zheshiyigemima");

			JedisShardInfo info2 = new JedisShardInfo("121.42.10.67", 6379);
			info2.setPassword("zheshiyigemima");

			List<JedisShardInfo> shards = Arrays.asList(info1, info2);

			ShardedJedisPool pool = new ShardedJedisPool(new JedisPoolConfig(), shards);

			ShardedJedis one = pool.getResource();

			ShardedJedisPipeline pipeline = one.pipelined();

			long start = System.currentTimeMillis();
			for (int i = 0; i < SIZE; i++) {
				pipeline.set("sppn" + i, "n" + i);
			}
			List<Object> results = pipeline.syncAndReturnAll();
			long end = System.currentTimeMillis();
			pool.returnResource(one);
			System.out.println("Pipelined@Pool SET: " + ((end - start) / 1000.0) + " seconds");
			pool.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
