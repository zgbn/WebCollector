package cn.vfire.demo.app.biquge;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class NovelListMode {

	@Getter
	@Setter
	private String title = "小说列表";

	/** 小说列表 */
	@Getter
	@Setter
	private List<NodeMode> novelist;

	/** 采集页面链接 */
	@Getter
	@Setter
	private String gatherLinkUrl;

	/** 采集时间 */
	@Getter
	@Setter
	private String gatherDate;

}
