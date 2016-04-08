package cn.vfire.web.collector2.lang.enums;

public enum CrawlerExpInfo {

	EXIST("已存在"), NET("网络访问异常"), STOP("抓取任务停止"), VALIDATE("数据校验错误");

	private StringBuffer info;

	private CrawlerExpInfo(String info) {
		this.info = new StringBuffer(info);
	}

	public String getInfo() {
		return this.info.toString();
	}

	public String getCode() {
		return this.toString();
	}

	/**
	 * 设置自定义错误信息。
	 * 
	 * @param info
	 *            支持{}方式替换后面的params对象。
	 * @param params
	 * @return
	 */
	public CrawlerExpInfo setInfo(String info, Object... params) {

		info = info.replaceAll("\\{\\}", "%s");

		for (int i = 0; i < params.length; i++) {
			if (params[i] == null) {
				params[i] = "null";
			}
			if (!(params[i] instanceof String)) {
				params[i] = params[i].toString();
			}
		}

		this.info.append("。").append(String.format(info, params));

		return this;
	}

}
