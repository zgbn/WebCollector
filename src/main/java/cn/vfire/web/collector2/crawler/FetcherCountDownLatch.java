package cn.vfire.web.collector2.crawler;

import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.Setter;

public class FetcherCountDownLatch extends CountDownLatch {

	/**
	 * 判断是否有处理失败
	 */
	@Getter
	@Setter
	private boolean flag = true;


	public FetcherCountDownLatch(int count) {
		super(count);
	}

}
