package cn.vfire.web.collector3.tools.crawler.snapshot;

import lombok.Getter;
import lombok.Setter;

class Info {
	/** 逻辑运行次数计数器 */
	@Setter
	@Getter
	private int runCount = 0;

	/** 发生异常次数计数器 */
	@Setter
	@Getter
	private int exceptionCount = 0;

	/** 过程中逻辑最短执行时间毫秒 */
	@Setter
	@Getter
	private long minRuntime = 0;

	/** 过程中逻辑最长执行时间毫秒 */
	@Setter
	@Getter
	private long maxRuntime = 0;

	/** 初始化时间 */
	@Setter
	@Getter
	private long inittime = System.currentTimeMillis();

	@Override
	public String toString() {
		return String.format("Obj=%s,inittime=%d,runCount=%d,exceptionCount=%d,minRuntime=%d,maxRuntime=%s", super.toString(), this.inittime, this.runCount,
				this.exceptionCount, this.minRuntime, this.maxRuntime);
	}
	
	
}