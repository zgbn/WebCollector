package cn.vfire.web.collector2.model;

import java.util.Comparator;
import java.util.TreeSet;

public class RsParams extends TreeSet<CrawlSnapshot> {

	private static final long serialVersionUID = 1L;


	public RsParams() {
		super(new Comparator<CrawlSnapshot>() {

			@Override
			public int compare(CrawlSnapshot rs1, CrawlSnapshot rs2) {
				boolean b1 = rs1.getTime() < rs2.getTime();
				return b1 ? -1 : 1;
			}
		});
	}

}
