package cn.vfire.web.collector3.crawler.visitor;

import java.util.List;

import cn.vfire.web.collector3.model.Links;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.ResultData;

public interface CrawlerVisitor {

	/**
	 * 从Page中提取数据
	 * 
	 * @param page
	 */
	public List<ResultData> fetchResultData(Page page, List<ResultData> resultData);

	/**
	 * 从Page中提取新的任务
	 * 
	 * @param page
	 * @param depth
	 *            当前页面深度
	 * @return
	 */
	public Links fetchParseLinks(Page page, Links links);

	/**
	 * 多个参与者执行顺序。
	 * 
	 * @return
	 */
	public int index();

}
