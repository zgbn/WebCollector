package cn.vfire.web.collector3.lang;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class CrawlerXmlFile {

	private File crawlerConfigXml;

	private File tmpCrawlerConfigXml;

	public CrawlerXmlFile(File crawlerConfigXml) throws IOException {
		this.crawlerConfigXml = crawlerConfigXml;
		this.tmpCrawlerConfigXml = this.getTmpFileToLowerCaseTag(crawlerConfigXml);
	}

	public CrawlerXmlFile(String crawlerConfigXml) throws IOException {
		this(new File(crawlerConfigXml));
	}

	public File getCrawlerConfigXml() {
		return crawlerConfigXml;
	}

	public File getTmpCrawlerConfigXml() {
		return tmpCrawlerConfigXml;
	}

	private File getTmpFileToLowerCaseTag(File xmlFile) throws IOException {

		String data = FileUtils.readFileToString(xmlFile, "UTF-8");

		File tmpFile = File.createTempFile("crawler", ".tmp");

		FileUtils.write(tmpFile, this.xmlDataToLowerCaseTag(data), "UTF-8", false);

		data = null;

		return tmpFile;

	}

	private String xmlDataToLowerCaseTag(String xmldata) throws IOException {

		StringBuffer sbdata = new StringBuffer(xmldata);

		Matcher matcher = Pattern.compile("(<[^\\s<>]+>?)|(\\s[^\\s<>\"]+=)").matcher(xmldata);

		int fromIndex = 0;

		while (matcher.find()) {

			String tagOrAttrName = matcher.group();

			{
				int tagOrAttrNameLength = tagOrAttrName.length();

				fromIndex = sbdata.indexOf(tagOrAttrName, fromIndex);

				sbdata.replace(fromIndex, fromIndex + tagOrAttrNameLength, tagOrAttrName.toLowerCase());
			}

		}

		return sbdata.toString();

	}

}
