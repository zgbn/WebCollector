package cn.vfire.web.collector3.plugin;

import java.util.List;

import cn.vfire.web.collector3.db.Generator;
import cn.vfire.web.collector3.db.Injector;
import cn.vfire.web.collector3.db.SegmentWriter;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.tools.pool.TaskPool;

public class BerkeleyTaskPool extends TaskPool implements SegmentWriter, Generator, Injector {

	public BerkeleyTaskPool(String id) {
		super(id);
	}

	@Override
	public void save(CrawlDatum crawlDatum) {

	}

	@Override
	public void initSegmentWriter() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void wrtieFetchSegment(CrawlDatum fetchDatum) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeRedirectSegment(CrawlDatum datum, String realUrl) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void wrtieParseSegment(List<CrawlDatum> parseDatums) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeSegmentWriter() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public cn.vfire.web.collector.model.CrawlDatum next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void open() throws Exception {
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
	public void inject(CrawlDatum datum) throws Exception {
		// TODO Auto-generated method stub

	}

}
