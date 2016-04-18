package cn.vfire.web.collector3.plugin.redis;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import cn.vfire.web.collector3.tools.Tools;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;

public class RedisThreadsTest {

	public static void main(String[] args) {

		int nThreads = 10;

		int size = 100;

		Jedis jedis = getJedis();

		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

		CountDownLatch countDownLatch = new CountDownLatch(size);

		long t1 = System.currentTimeMillis();

		for (int i = 0; i < size; i++) {
			executorService.execute(new R(i, jedis, countDownLatch));
		}

		executorService.shutdown();

		long t2 = System.currentTimeMillis();

		try {
			countDownLatch.await();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		jedis.disconnect();

		System.out.println(String.format("测试Redis多线程单实例set操作，线程数%d个，任务数%d个，总耗时%s。", nThreads, size,
				Tools.TimeFormat.AUTO.format(t2 - t1)));

	}


	public static class R implements Runnable {

		private static AtomicInteger buffsize = new AtomicInteger(0);

		private int i;

		private Jedis jedis;

		private CountDownLatch countDownLatch;


		private R(int i, Jedis jedis, CountDownLatch countDownLatch) {
			this.i = i;
			this.jedis = jedis;
			this.countDownLatch = countDownLatch;
		}


		public void run() {

			this.countDownLatch.countDown();

			Pipeline pipeline = jedis.pipelined();

			pipeline.set("p" + i, "p" + i);

			if (buffsize.incrementAndGet() % 100 == 0) {
				List<Object> results = pipeline.syncAndReturnAll();
				System.out.println("Pipelined SET 100 BUFFSIZE IS " + results.size());
			}

		}

	}


	public static Jedis getJedis() {
		JedisShardInfo info = new JedisShardInfo("121.42.10.67", 6379);
		info.setPassword("zheshiyigemima");
		Jedis jedis = new Jedis(info);
		return jedis;
	}

}
