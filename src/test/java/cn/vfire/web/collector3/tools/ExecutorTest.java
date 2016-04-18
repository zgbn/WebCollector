package cn.vfire.web.collector3.tools;

import org.junit.Test;

import cn.vfire.web.collector3.crawler.defaults.DefaultExecutor;
import cn.vfire.web.collector3.crawler.executor.Executor;
import cn.vfire.web.collector3.model.CrawlDatum;
import cn.vfire.web.collector3.model.Page;

public class ExecutorTest {

	private Executor executor = new DefaultExecutor();

	@Test
	public void testExecute() {

		String url = "http://www.biquge.la/book/4522/2860791.html";

		try {

			CrawlDatum crawlDatum = new CrawlDatum(url, 1);

			Page page = executor.execute(crawlDatum);

			System.out.println(page.getHtml());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteProxyIp() {

		String url = "http://www.biquge.la/book/4522/2860791.html";

		String proxyIp = "202.106.16.36";

		int proxyPort = 3128;

		try {

			CrawlDatum crawlDatum = new CrawlDatum(url, proxyIp, proxyPort, 1);

			Page page = executor.execute(crawlDatum);

			System.out.println(page.getHtml());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
