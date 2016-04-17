package cn.vfire.web.collector3.lang.enums;

public enum CrawlerExpInfo {

	LOCK("数据库乐观锁操作异常"),
	STATEMENT_DELETE("数据库操作Delete异常"),
	STATEMENT_SELECT("数据库操作Select异常"),
	STATEMENT_UPDATE("数据库操作Update异常"),
	STATEMENT_INSERT("数据库操作Inset异常"),
	STATEMENT("数据库操作异常"),
	FAIL("失败"),
	NOTEXIST("不存在"),
	EXIST("已存在"),
	NET("网络访问异常"),
	STOP("强制停止异常"),
	VALIDATE("数据校验没有通过异常");

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
