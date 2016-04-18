package cn.vfire.web.collector3.tools.crawler.format.impl;

import java.util.List;

import cn.vfire.web.collector3.model.ResultData;
import cn.vfire.web.collector3.tools.crawler.format.FormatData;

public class DefaultDataFormat implements FormatData {

	@Override
	public void setResultData(List<ResultData> rdata) {

		if (rdata != null && rdata.isEmpty() == false) {

			if (rdata.size() == 1) {
				System.out.println(rdata.get(0).toResultData());
			} else {
				for (ResultData data : rdata) {
					System.out.println(data.toResultData());
				}
			}

		}

	}

}
