package cn.vfire.web.collector3.crawler.pool;

import cn.vfire.web.collector3.crawler.Default;
import cn.vfire.web.collector3.lang.CrawlerException;
import cn.vfire.web.collector3.model.CrawlDatum;

public class DefaultTaskPool extends TaskPool implements Default {

	@Override
	public CrawlDatum next() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void open() throws CrawlerException {
		// TODO Auto-generated method stub

	}


	@Override
	public void setTopN(int topN) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setMaxExecuteCount(int maxExecuteCount) {
		// TODO Auto-generated method stub

	}


	@Override
	public int getTotalGenerate() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}


	@Override
	public Generator getGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

}
