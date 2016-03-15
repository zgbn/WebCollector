package cn.vfire.demo.webcrawler;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.vfire.demo.app.biquge.ChapterMode;
import cn.vfire.demo.app.biquge.NodeMode;
import cn.vfire.demo.app.biquge.NovelListMode;
import cn.vfire.demo.app.biquge.NovelMode;

/**
 * Crawling news from hfut news
 *
 * @author hu
 */
public class BiqugeCrawler extends BreadthCrawler {

	private ConcurrentSkipListMap<String, Integer> excptionCounts = new ConcurrentSkipListMap<String, Integer>();

	private static String Title = "小说列表";

	private NovelListMode novelListMode;

	private String seed = "http://www.biquge.la/xiaoshuodaquan/";

	private String regular1 = "http://www\\.biquge\\.la/book/[0-9]+/?";

	private String regular2 = "http://www\\.biquge\\.la/book/[0-9]+/[0-9]+\\.html";


	/**
	 * @param crawlPath
	 *            crawlPath is the path of the directory which maintains
	 *            information of this crawler
	 * @param autoParse
	 *            if autoParse is true,BreadthCrawler will auto extract links
	 *            which match regex rules from pag
	 */
	public BiqugeCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		/* start page */
		this.addSeed(seed);

		this.addRegex(regular1);
		this.addRegex(regular2);
		/* do not fetch jpg|png|gif */
		this.addRegex("-.*\\.(jpg|png|gif).*");
		/* do not fetch url contains # */
		this.addRegex("-.*#.*");
	}


	public void visit(Page page, CrawlDatums next) {

		final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

		String gatherDate = null;
		String gatherLinkUrl = null;
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			gatherDate = dateFormat.format(new Date(System.currentTimeMillis()));
			gatherLinkUrl = page.getUrl();
		}

		if (page.matchUrl(this.seed)) {

			this.novelListMode = new NovelListMode();

			this.novelListMode.setTitle(Title);
			this.novelListMode.setGatherDate(gatherDate);
			this.novelListMode.setGatherLinkUrl(gatherLinkUrl);
			this.novelListMode.setNovelist(new ArrayList<NodeMode>());

			Elements eleNovellist = page.select("#main > div.novellist");

			List<NodeMode> novellist = this.novelListMode.getNovelist();

			int no = 1;

			for (Element subNovelList : eleNovellist) {

				String classify = subNovelList.select("h2").text();

				Elements novelNodes = subNovelList.select("ul > li");

				for (int i = 0; i < novelNodes.size(); i++) {

					Element e = novelNodes.get(i);
					Elements a = e.select("a");

					NodeMode mode = new NodeMode();
					mode.setIdx(no++);
					mode.setName(a.text());
					mode.setClassify(classify);
					mode.setLink(a.attr("href"));
					mode.setInfo(e.text());

					novellist.add(mode);
				}

			}

			{
				String path = "/BiqugeCrawler/";
				String name = String.format("%s.json", this.novelListMode.getTitle());
				String content = gson.toJson(this.novelListMode);

				this.writeFile(path, name, content);
			}
		}

		if (page.matchUrl(this.regular1)) {

			if (this.novelListMode == null) { return; }

			NovelMode mode = new NovelMode();

			mode.setGatherDate(gatherDate);
			mode.setGatherLinkUrl(gatherLinkUrl);
			mode.setFmimg(page.select("#fmimg > img").attr("src"));
			mode.setTitle(page.select("#info > h1").text());
			mode.setAuthor(page.select("#info > p:nth-child(2)").text());
			mode.setUpdate(page.select("#info > p:nth-child(4)").text());
			mode.setLatestChapter(page.select("#info > p:nth-child(5) > a").text());
			mode.setIntroduction(page.select("#intro").text());
			mode.setChapterDirectory(new ArrayList<NodeMode>());
			{
				List<NodeMode> directorys = mode.getChapterDirectory();
				Elements eles = page.select("#list > dl > dd");

				for (int i = 0; i < eles.size(); i++) {
					Element e = eles.get(i);
					Elements a = e.select("a") ;
					NodeMode node = new NodeMode() ;
					node.setIdx(i+1); 
					node.setLink(a.attr("href"));
					node.setName(a.text());
					
					directorys.add(node);
				}
			}

			{
				String path = String.format("/BiqugeCrawler/%s/%s/", this.novelListMode.getTitle(), mode.getTitle());
				String name = String.format("_Novel_%s.json", mode.getTitle());
				String content = gson.toJson(mode);

				this.writeFile(path, name, content);
			}

		}

		if (page.matchUrl(this.regular2)) {

			if (this.novelListMode == null) { return; }

			ChapterMode mode = new ChapterMode();

			mode.setGatherDate(gatherDate);
			mode.setGatherLinkUrl(gatherLinkUrl);

			mode.setNovelTitle(page.select("#wrapper > div.content_read > div > div.con_top > a", 1).text());
			mode.setTitle(page.select("#wrapper > div.content_read > div > div.bookname > h1").text());

			Elements eContent = page.select("#content");
			mode.setContent(eContent.text());
			mode.setHtmlContent(eContent.html());

			String path = String.format("/BiqugeCrawler/%s/%s/", this.novelListMode.getTitle(), mode.getNovelTitle());
			String name = String.format("%s.json", mode.getTitle());
			String content = gson.toJson(mode);

			{
				this.writeFile(path, name, content);
			}

		}

		// String title = page.select("div[id=Article]>h2").first().text();
		// String content = page.select("div#artibody", 0).text();
	}

	private void writeFile(String filePath, String fileName, String data) {

		String file = fileName.replaceAll("[\\\\/:*?\"<>|]+", "");

		File outfile = new File(filePath, file);

		try {
			FileUtils.write(outfile, data, "UTF-8", false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(outfile.getPath());
	}


	@Override
	public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
		/*
		 * WebCollector自带一个Proxys类，通过Proxys.nextRandom()方法可以随机获取加入的代理。
		 * 通过Proxys.add(“ip”,”端口号”);添加代理。
		 * 如果本机也参与http请求，可用Proxys.addEmpty()方法将本机加入。
		 *
		 * 覆盖Crawler的getResponse()方法，即可自定义使用随机代理的http请求： HttpRequest request =
		 * new HttpRequest(crawlDatum); request.setProxy(proxys.nextRandom());
		 * return request.getResponse();
		 * 代码中的proxys并不是一个自带的对象，用户需要在继承Crawler时，定义一个成员变量： Proxys proxys=new
		 * Proxys();
		 */
		HttpRequest request = new HttpRequest(crawlDatum);
		request.setTimeoutForConnect(10000);
		request.setTimeoutForRead(10000);

		return request.getResponse();

	}


	public static void main(String[] args) throws Exception {
		BiqugeCrawler crawler = new BiqugeCrawler("crawl", true);
		crawler.setThreads(10);
		// crawler.setTopN(5);
		crawler.setResumable(false);
		/* start crawl with depth of 4 */
		crawler.start(1);
	}

}
