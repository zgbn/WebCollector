package cn.vfire.common.utils.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageTest {

	private LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(10);

	public static void main(String[] args) {

		StorageTest s = new StorageTest();

		new Thread(s.new Consumer()).start();
		new Thread(s.new Producer()).start();
		new Thread(s.new Producer()).start();

	}

	public class Producer implements Runnable {

		@Override
		public void run() {

			String name = Thread.currentThread().getName();

			while (true) {

				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				try {
					System.out.println(name + ":+" + 1);
					queue.put(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class Consumer implements Runnable {

		@Override
		public void run() {

			String name = Thread.currentThread().getName();

			while (true) {

				try {
					Thread.sleep(600);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				try {
					int i = queue.take();
					System.out.println(name + ":-" + i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
