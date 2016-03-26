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
package cn.vfire.web.collector.crawldb;

import cn.vfire.web.collector.model.CrawlDatum;
import cn.vfire.web.collector.model.CrawlDatums;
import cn.vfire.web.collector.model.Links;

/**
 * 数据库管家工具抽象类
 */
public abstract class DBManager implements Injector, SegmentWriter, DBLock {

	/**
	 * 数据库对象是否存在
	 * 
	 * @return
	 */
	public abstract boolean isDBExists();

	public abstract void clear() throws Exception;

	/**
	 * 获取任务解析器
	 * 
	 * @return
	 */
	public abstract Generator getGenerator();

	/**
	 * 发开数据库资源
	 * 
	 * @throws Exception
	 */
	public abstract void open() throws Exception;

	public abstract void close() throws Exception;

	/**
	 * 注入任务对象
	 * 
	 * @param datum
	 *            任务对象
	 * @param force
	 *            直接
	 * @throws Exception
	 */
	public abstract void inject(CrawlDatum datum, boolean force) throws Exception;

	/**
	 * 合并任务
	 * 
	 * @throws Exception
	 */
	public abstract void merge() throws Exception;

	/**
	 * 注入任务对象。
	 */
	@Override
	public void inject(CrawlDatum datum) throws Exception {
		inject(datum, false);
	}

	/**
	 * 注入任务对象集合
	 * 
	 * @param datums
	 * @param force
	 * @throws Exception
	 */
	public void inject(CrawlDatums datums, boolean force) throws Exception {
		for (CrawlDatum datum : datums) {
			inject(datum, force);
		}
	}

	/**
	 * 注入任务对象集合
	 * 
	 * @param datums
	 * @throws Exception
	 */
	public void inject(CrawlDatums datums) throws Exception {
		inject(datums, false);
	}

	/**
	 * 注入任务对象集合
	 * 
	 * @param links
	 * @param force
	 * @throws Exception
	 */
	public void inject(Links links, boolean force) throws Exception {
		CrawlDatums datums = new CrawlDatums(links);
		inject(datums, force);
	}

	/**
	 * 注入任务对象集合
	 * 
	 * @param links
	 * @throws Exception
	 */
	public void inject(Links links) throws Exception {
		inject(links, false);
	}

	/**
	 * 注入任务对象
	 * 
	 * @param url
	 * @param force
	 * @throws Exception
	 */
	public void inject(String url, boolean force) throws Exception {
		CrawlDatum datum = new CrawlDatum(url);
		inject(datum, force);
	}

	/**
	 * 注入任务对象
	 * 
	 * @param url
	 * @throws Exception
	 */
	public void inject(String url) throws Exception {
		CrawlDatum datum = new CrawlDatum(url);
		inject(datum);
	}

}
