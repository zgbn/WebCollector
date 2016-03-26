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

/**
 * 数据库操作锁管理接口
 */
public interface DBLock {

	/**
	 * 获取锁
	 * 
	 * @throws Exception
	 */
	public void lock() throws Exception;

	/**
	 * 判断是否被锁
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isLocked() throws Exception;

	/**
	 * 解锁
	 * 
	 * @throws Exception
	 */
	public void unlock() throws Exception;
}
