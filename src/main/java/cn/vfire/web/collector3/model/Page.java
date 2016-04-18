package cn.vfire.web.collector3.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.vfire.web.collector3.net.HttpResponse;
import cn.vfire.web.utils.CharsetDetector;

/**
 * Page是爬取过程中，内存中保存网页爬取信息的一个容器，Page只在内存中存 放，用于保存一些网页信息，方便用户进行自定义网页解析之类的操作。
 */
@Slf4j
public class Page {

	@Getter
	private CrawlDatum crawlDatum;

	@Getter
	private HttpResponse response;

	private String html;

	@Getter
	private Document doc;

	@Getter
	@Setter
	private String charset = null;

	/**
	 * 响应时间
	 */
	@Getter
	@Setter
	private long responseTime;


	/**
	 * 判断当前Page的URL是否和输入正则匹配
	 * 
	 * @param urlRegex
	 * @return
	 */
	public boolean matchUrl(String urlRegex) {
		return Pattern.matches(urlRegex, getUrl());
	}


	/**
	 * 获取网页中满足指定css选择器的所有元素的指定属性的集合
	 * 例如通过getAttrs("img[src]","abs:src")可获取网页中所有图片的链接
	 * 
	 * @param cssSelector
	 * @param attrName
	 * @return
	 */
	public ArrayList<String> getAttrs(String cssSelector, String attrName) {
		ArrayList<String> result = new ArrayList<String>();
		Elements eles = select(cssSelector);
		for (Element ele : eles) {
			if (ele.hasAttr(attrName)) {
				result.add(ele.attr(attrName));
			}
		}
		return result;
	}


	/**
	 * 获取满足选择器的元素中的链接 选择器cssSelector必须定位到具体的超链接 例如我们想抽取id为content的div中的所有超链接，这里
	 * 就要将cssSelector定义为div[id=content] a
	 *
	 * @param cssSelector
	 * @return
	 */
	public Links getLinks(String cssSelector, int depth) {
		Links links = new Links().addBySelector(doc(), cssSelector, depth);
		return links;
	}


	public Elements select(String cssSelector) {
		return this.doc().select(cssSelector);
	}


	public Element select(String cssSelector, int index) {
		Elements eles = select(cssSelector);
		int realIndex = index;
		if (index < 0) {
			realIndex = eles.size() + index;
		}
		return eles.get(realIndex);
	}


	public String regex(String regex, int group, String defaultResult) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(getHtml());
		if (matcher.find()) {
			return matcher.group(group);
		}
		else {
			return defaultResult;
		}
	}


	public String regex(String regex, int group) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(getHtml());
		matcher.find();
		return matcher.group(group);
	}


	public String regex(String regex, String defaultResult) {
		return regex(regex, 0, defaultResult);
	}


	public String regex(String regex) {
		return regex(regex, 0);
	}


	public Page(CrawlDatum datum, HttpResponse response) {
		this.crawlDatum = datum;
		this.response = response;
	}


	/**
	 * 返回网页/文件的内容
	 *
	 * @return 网页/文件的内容
	 */
	public byte[] getContent() {
		if (response == null) {
			return null;
		}
		return response.getContent();
	}


	/**
	 * 返回网页的url
	 *
	 * @return 网页的url
	 */
	public String getUrl() {
		return crawlDatum.getUrl();
	}


	/**
	 * 返回网页的源码字符串
	 *
	 * @return 网页的源码字符串
	 */
	public String getHtml() {
		if (html != null) {
			return html;
		}
		if (response.getHtml() != null) {
			html = response.getHtml();
			return html;
		}
		if (getContent() == null) {
			return null;
		}
		if (charset == null) {
			charset = CharsetDetector.guessEncoding(getContent());
		}
		try {
			this.html = new String(getContent(), charset);
			return html;
		}
		catch (UnsupportedEncodingException ex) {
			log.warn("返回页面HTML源码发生异常。", ex);
			return null;
		}
	}


	/**
	 * 返回网页解析后的DOM树(Jsoup的Document对象)
	 * 
	 * @return
	 */
	public Document doc() {
		if (doc != null) {
			return doc;
		}
		try {

			this.doc = Jsoup.parse(getHtml(), getUrl());
			return doc;
		}
		catch (Exception ex) {
			log.warn("返回页面HTML转化的Document对象发生异常。", ex);
			return null;
		}
	}


	public HashMap<String, String> getMetaData() {
		return crawlDatum.getMetaData();
	}


	public void setMetaData(HashMap<String, String> metaData) {
		this.crawlDatum.setMetaData(metaData);
	}


	public void meta(String key, String value) {
		this.crawlDatum.meta(key, value);
	}


	public String meta(String key) {
		return this.crawlDatum.meta(key);
	}

}
