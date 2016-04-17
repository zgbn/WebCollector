package cn.vfire.web.collector3.crawler.plugin.berkeley;

import org.junit.Test;

import cn.vfire.web.collector3.db.Statement;
import cn.vfire.web.collector3.model.CrawlDatum;

public class BerkeleyDBManagerTest {

	private BerkeleyDBManager db = new BerkeleyDBManager("d:/BerkeleyDB/data");


	@Test
	public void app() {

		try {

			if (db.isExist()) {
				db.clean();
			}

			db.open();

			Statement statement = db.createStatement("TestDB");

			CrawlDatum crawlDatum = statement.select("abd");

			statement.close();

			System.out.println(crawlDatum);

			db.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
