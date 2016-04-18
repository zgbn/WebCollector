/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.vfire.web.collector3.crawldb;

import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.CrawlDatums;
import cn.vfire.web.collector3.model.Links;

/**
 * 数据库管家工具抽象类
 */
public abstract class DBManager implements Injector, SegmentWriter {

	protected Injector injector;

	protected SegmentWriter segmentWriter;

	public abstract boolean isDBExists();

	public abstract void clear() throws CrawlerDBException;

	public abstract Generator getGenerator();

	public abstract void open() throws CrawlerDBException;

	public abstract void close() throws CrawlerDBException;

	@Override
	public void wrtieParseSegment(Links links) throws CrawlerDBException {
		this.wrtieParseSegment(new CrawlDatums(links));
	}

	@Override
	public void initSegmentWriter() throws CrawlerDBException {
		this.segmentWriter.initSegmentWriter();
	}

	@Override
	public void wrtieFetchSegment(CrawlDatum fetchDatum) throws CrawlerDBException {
		this.segmentWriter.wrtieFetchSegment(fetchDatum);
	}

	@Override
	public void deleteFetchSegment(CrawlDatum fetchDatum) throws CrawlerDBException {
		this.segmentWriter.deleteFetchSegment(fetchDatum);
	}

	@Override
	public void wrtieParseSegment(CrawlDatums parseDatums) throws CrawlerDBException {
		this.segmentWriter.wrtieParseSegment(parseDatums);
	}

	@Override
	public void closeSegmentWriter() throws CrawlerDBException {
		this.segmentWriter.closeSegmentWriter();
	}

	@Override
	public void inject(CrawlDatum datum) throws CrawlerDBException {
		this.injector.inject(datum);
	}
	
	

}
