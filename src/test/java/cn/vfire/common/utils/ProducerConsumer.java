package cn.vfire.common.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * java多线程模拟生产者消费者问题
 * 
 * ProducerConsumer是主类，Producer生产者，Consumer消费者，Product产品，Storage仓库
 * 
 * @author 林计钦
 * @version 1.0 2013-7-24 下午04:49:02
 */
public class ProducerConsumer {

	public static void main(String[] args) {

		ProducerConsumer pc = new ProducerConsumer();

		Storage<Product> s = pc.productStorage;

		ExecutorService service = Executors.newCachedThreadPool();
		Producer<Product> p = pc.new Producer<Product>("张三", s);
		Consumer<Product> c = pc.new Consumer<Product>("王五", s);
		Consumer<Product> c2 = pc.new Consumer<Product>("老刘", s);
		Consumer<Product> c3 = pc.new Consumer<Product>("老林", s);

		service.submit(p);

		service.submit(c);
		service.submit(c2);
		service.submit(c3);

	}

	/**
	 * 消费者
	 * 
	 * @author 林计钦
	 * @version 1.0 2013-7-24 下午04:53:30
	 */
	class Consumer<T> implements Runnable {

		private String name;

		private Storage<T> s = null;

		public Consumer(String name, Storage<T> s) {
			this.name = name;
			this.s = s;
		}

		public void run() {
			try {
				while (true) {
					System.out.println(name + "准备消费产品.");
					T product = s.pop();
					if (product == null) {
						break;
					}
					System.out.println(name + "已消费(" + product.toString() + ").");
					System.out.println("===============");
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 生产者
	 * 
	 * @author 林计钦
	 * @version 1.0 2013-7-24 下午04:53:44
	 */
	class Producer<T> implements Runnable {

		private String name;

		private Storage<T> s = null;

		public Producer(String name, Storage<T> s) {
			this.name = name;
			this.s = s;
		}

		public void run() {
			try {
				while (true) {

					T product = s.create();

					if (product == null) {
						s.push(null);
						break;
					} else {
						s.push(product);
						System.out.println(name + "已生产(" + product.toString() + ").");
						System.out.println("===============");
					}

					Thread.sleep(50);
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * 仓库，用来存放产品
	 * 
	 * @author 林计钦
	 * @version 1.0 2013-7-24 下午04:54:16
	 */
	public abstract class Storage<T> {

		private BlockingQueue<T> queues = new LinkedBlockingQueue<T>(1000);

		/**
		 * 生产
		 * 
		 * @param p
		 *            产品
		 * @throws InterruptedException
		 */
		public void push(T p) throws InterruptedException {
			queues.put(p);
		}

		/**
		 * 消费
		 * 
		 * @return 产品
		 * @throws InterruptedException
		 */
		public T pop() throws InterruptedException {
			return queues.take();
		}

		public abstract T create();
	}

	private Storage<Product> productStorage = new Storage<Product>() {

		private int num = 1000;

		private AtomicInteger count = new AtomicInteger(0);

		@Override
		public Product create() {
			System.out.println("========================：" + count);
			if (count.get() >= num) {
				return null;
			} else {
				count.incrementAndGet();
				Product product = new Product((int) (Math.random() * 10000)); // 产生0~9999随机整数
				return product;
			}
		}
	};

	/**
	 * 产品
	 * 
	 * @author 林计钦
	 * @version 1.0 2013-7-24 下午04:54:04
	 */
	public class Product {
		private int id;

		public Product(int id) {
			this.id = id;
		}

		public String toString() {// 重写toString方法
			return "产品：" + this.id;
		}
	}

}