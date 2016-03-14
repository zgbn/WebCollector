package cn.vfire.demo.app.biquge;

import lombok.Getter;
import lombok.Setter;

public class ChapterMode {

	/** 小说标题 */
	@Getter
	@Setter
	private String novelTitle;

	/** 章节标题 */
	@Getter
	@Setter
	private String title;

	/** 纯文本正文 */
	@Getter
	@Setter
	private String content;

	/** html格式正文 */
	@Getter
	@Setter
	private String htmlContent;

	/** 采集页面链接 */
	@Getter
	@Setter
	private String gatherLinkUrl;

	/** 采集时间 */
	@Getter
	@Setter
	private String gatherDate;

}
