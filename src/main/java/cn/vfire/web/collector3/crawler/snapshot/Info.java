package cn.vfire.web.collector3.crawler.snapshot;

import cn.vfire.web.collector3.annotation.Label;
import cn.vfire.web.collector3.tools.Tools;
import lombok.Getter;
import lombok.Setter;

class Info {

	/** 逻辑运行次数计数器 */
	@Setter
	@Getter
	@Label("逻辑运行次数计数器")
	private int runCount = 0;

	/** 发生异常次数计数器 */
	@Setter
	@Getter
	@Label("发生异常次数计数器 ")
	private int exceptionCount = 0;

	/** 过程中逻辑最短执行时间毫秒 */
	@Setter
	@Getter
	@Label("过程中逻辑最短执行时间毫秒 ")
	private long minRuntime = 0;

	/** 过程中逻辑最长执行时间毫秒 */
	@Setter
	@Getter
	@Label("过程中逻辑最长执行时间毫秒 ")
	private long maxRuntime = 0;

	/** 初始化时间 */
	@Setter
	@Getter
	@Label("初始化时间 ")
	private long inittime = System.currentTimeMillis();


	@Override
	public String toString() {
		return Tools.toStringByFieldLabel(this);
	}

}