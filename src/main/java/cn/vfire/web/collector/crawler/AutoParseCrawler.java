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
package cn.vfire.web.collector.crawler;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vfire.web.collector.fetcher.Executor;
import cn.vfire.web.collector.fetcher.Visitor;
import cn.vfire.web.collector.model.CrawlDatum;
import cn.vfire.web.collector.model.CrawlDatums;
import cn.vfire.web.collector.model.Links;
import cn.vfire.web.collector.model.Page;
import cn.vfire.web.collector.net.HttpRequest;
import cn.vfire.web.collector.net.HttpResponse;
import cn.vfire.web.collector.net.Requester;
import cn.vfire.web.collector.util.RegexRule;

/**
 * @author ChenGang
 */
public abstract class AutoParseCrawler extends Crawler implements Executor, Visitor, Requester {

	public static final Logger LOG = LoggerFactory.getLogger(AutoParseCrawler.class);

	/**
	 * 是否自动抽取符合正则的链接并加入后续任务
	 */
	protected boolean autoParse = true;

	/**
	 * 声明一个观察者。
	 */
	protected Visitor visitor;

	/** 声明一个真正的任务执行者。 */
	protected Requester requester;

	/**
	 * URL正则约束集合
	 */
	protected RegexRule regexRule = new RegexRule();

	/**
	 * 构造器。
	 * 
	 * @param autoParse
	 */
	public AutoParseCrawler(boolean autoParse) {
		this.autoParse = autoParse;
		this.visitor = this;
		this.requester = this;
		this.executor = this;
	}

	/**
	 * 添加URL正则约束，用于提取URL
	 *
	 * @param urlRegex
	 */
	public void addRegex(String urlRegex) {
		regexRule.addRule(urlRegex);
	}

	/**
	 * 任务执行调度方法
	 */
	public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
		HttpResponse response = requester.getResponse(datum);
		Page page = new Page(datum, response);
		visitor.visit(page, next);
		if (autoParse && !regexRule.isEmpty()) {
			// 自动分析当前子页面的所有URL连接
			parseLink(page, next);
		}
	}

	/**
	 *
	 * @return
	 */
	public RegexRule getRegexRule() {
		return regexRule;
	}

	public Requester getRequester() {
		return requester;
	}

	/**
	 * 对真正任务执行者的具体实现。
	 */
	public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
		HttpRequest request = new HttpRequest(crawlDatum);
		return request.getResponse();
	}

	public Visitor getVisitor() {
		return visitor;
	}

	/**
	 *
	 * @return 返回是否自动抽取符合正则的链接并加入后续任务
	 */
	public boolean isAutoParse() {
		return autoParse;
	}

	/**
	 * 分析当前任务结果，从结果中提取新的任务
	 * 
	 * @param page
	 * @param next
	 */
	protected void parseLink(Page page, CrawlDatums next) {
		String conteType = page.getResponse().getContentType();
		if (conteType != null && conteType.contains("text/html")) {
			Document doc = page.doc();
			if (doc != null) {
				// 提取所有的满足约束的URL
				Links links = new Links().addByRegex(doc, regexRule);
				// 添加到此任务的下一个任务处理。
				next.add(links);
			}
		}
	}

	/**
	 * 设置是否自动抽取符合正则的链接并加入后续任务
	 *
	 * @param autoParse
	 */
	public void setAutoParse(boolean autoParse) {
		this.autoParse = autoParse;
	}

	/**
	 *
	 * @param regexRule
	 */
	public void setRegexRule(RegexRule regexRule) {
		this.regexRule = regexRule;
	}

	public void setRequester(Requester requester) {
		this.requester = requester;
	}

	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}
}
