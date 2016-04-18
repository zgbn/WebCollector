package cn.vfire.web.collector3.plugin.berkeley;

import cn.vfire.web.collector3.crawldb.DBManager;
import cn.vfire.web.collector3.lang.CrawlerDBException;
import cn.vfire.web.collector3.model.CrawlDatum;

public class BerkeleyDBManagerTest {

	public static void main(String[] args) {

		try {

			DBManager dbManager = new BerkeleyDBManager();

			boolean dbExists = dbManager.isDBExists();

			if (dbExists) {
				dbManager.clear();
			}

			((BerkeleyDBManager) dbManager).setName("TestApp");

			dbManager.open();

			dbManager.inject(new CrawlDatum("A1", 1));
			dbManager.inject(new CrawlDatum("A2", 1));

			dbManager.close();

		} catch (CrawlerDBException e) {
			e.printStackTrace();
		}

	}

}
