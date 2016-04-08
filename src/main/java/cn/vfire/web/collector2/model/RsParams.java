package cn.vfire.web.collector2.model;

import java.util.Comparator;
import java.util.TreeSet;

public class RsParams extends TreeSet<RsParam> {

	private static final long serialVersionUID = 1L;


	public RsParams() {
		super(new Comparator<RsParam>() {

			@Override
			public int compare(RsParam rs1, RsParam rs2) {
				boolean b1 = rs1.getTime() < rs2.getTime();
				return b1 ? -1 : 1;
			}
		});
	}

}
