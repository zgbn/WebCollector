package cn.vfire.web.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程Sleep工具类
 * 
 * @author ChenGang
 *
 */
@Slf4j
public final class ThreadSleep {

	public static void sleep(int millisecond) {
		try {
			Thread.sleep(millisecond);
		}
		catch (InterruptedException e) {
			log.info("线程等待异常。", e);
		}
	}


	public static void sleepSecond(int second) {
		try {
			Thread.sleep(second * 1000);
		}
		catch (InterruptedException e) {
			log.info("线程等待异常。", e);
		}
	}


	public static void sleepMinute(int minute) {
		try {
			Thread.sleep(minute * 60 * 1000);
		}
		catch (InterruptedException e) {
			log.info("线程等待异常。", e);
		}
	}

}
