package cn.vfire.demo.app.biquge;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class NovelMode {

	/** 小说封面图片 */
	@Getter
	@Setter
	private String fmimg;

	/** 小说标题 */
	@Getter
	@Setter
	private String title;

	/** 小说作者 */
	@Getter
	@Setter
	private String author;

	/** 最后更新时间 */
	@Getter
	@Setter
	private String update;

	/** 最新章节 */
	@Getter
	@Setter
	private String latestChapter;

	/** 小说简介 */
	@Getter
	@Setter
	private String introduction;

	/** 章节目录 */
	@Getter
	@Setter
	private List<NodeMode> chapterDirectory;

	/** 采集页面链接 */
	@Getter
	@Setter
	private String gatherLinkUrl;

	/** 采集时间 */
	@Getter
	@Setter
	private String gatherDate;

}
