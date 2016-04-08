package cn.vfire.web.collector2.model;

import lombok.Getter;

public class RsParam {

	/** 响应时间毫秒 */
	@Getter
	private int time;

	/** 异常信息 */
	private Exception exception;


	public RsParam(int time) {
		this.time = time;
	}


	@Override
	public boolean equals(Object obj) {

		if (obj instanceof RsParam) {

			RsParam rs = (RsParam) obj;

			boolean b1 = this.time == rs.time;
			boolean b2 = this.exception.getClass() == rs.exception.getClass();

			return b1 && b2;

		}

		return false;
	}


	

	
	
}
