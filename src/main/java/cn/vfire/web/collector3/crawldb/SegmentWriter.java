/*
 * Copyright (C) 2014 hu
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
 * 爬取过程中，写入爬取历史、网页Content、解析信息的Writer
 */
public interface SegmentWriter {

	/***
	 * 初始化爬去任务写配置
	 * 
	 * @throws Exception
	 */
	public void initSegmentWriter() throws CrawlerDBException;

	/**
	 * 写入直接取任务
	 * 
	 * @param fetchDatum
	 * @throws Exception
	 */
	public void wrtieFetchSegment(CrawlDatum fetchDatum) throws CrawlerDBException;

	/**
	 * 直接删除任务
	 * 
	 * @param fetchDatum
	 * @throws CrawlerDBException
	 */
	public void deleteFetchSegment(CrawlDatum fetchDatum) throws CrawlerDBException;

	/**
	 * 写入解析后结果的任务集合。
	 * 
	 * @param parseDatums
	 * @throws Exception
	 */
	public void wrtieParseSegment(CrawlDatums parseDatums) throws CrawlerDBException;

	/**
	 * 写入解析后结果的任务集合。
	 * 
	 * @param links
	 * @throws CrawlerDBException
	 */
	public void wrtieParseSegment(Links links) throws CrawlerDBException;

	/**
	 * 关闭写动作。
	 * 
	 * @throws Exception
	 */
	public void closeSegmentWriter() throws CrawlerDBException;

}
