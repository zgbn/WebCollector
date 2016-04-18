package cn.vfire.web.collector3.plugin.redis;

import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;

public class RedisDBManagerTest {

	public static void main(String[] args) {

		DBManager dbManager = new RedisDBManager();

		try {

			if (dbManager.isDBExists()) {
				dbManager.clear();
			}

			dbManager.open();

			dbManager.inject(new CrawlDatum("A", 1));
			dbManager.inject(new CrawlDatum("B", 1));
			dbManager.inject(new CrawlDatum("C", 1));
			dbManager.inject(new CrawlDatum("D", 1));
			
			dbManager.close();

		} catch (CrawlerDBException e) {
			e.printStackTrace();
		}

	}

}
