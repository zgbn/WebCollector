package cn.vfire.web.collector3.crawler.visitor;

import java.util.List;

import cn.vfire.web.collector3.crawler.pool.TaskPool;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.ResultData;

public interface CrawlerVisitor {

	/**
	 * 从Page中提取数据
	 * 
	 * @param page
	 */
	public List<ResultData> fetchResultData(Page page);


	/**
	 * 从Page中提取新的任务
	 * 
	 * @param page
	 * @param taskPool
	 */
	public void fetchCrawlDatum(Page page, TaskPool taskPool);

}
