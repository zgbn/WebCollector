package cn.vfire.web.collector3.tools.crawler.element;

import lombok.Getter;

import org.w3c.dom.Node;

import cn.vfire.web.collector3.crawler.snapshot.CatchSnapshotInfo;
import cn.vfire.web.collector3.lang.CrawlerConfigXmlException;
import cn.vfire.web.collector3.tools.crawler.Element;

import com.google.gson.annotations.Expose;

public class Snapshot extends Element<Snapshot> {

	private static final long serialVersionUID = 1L;

	@Expose
	@Getter
	private int runtime;

	@Expose
	@Getter
	private int exceptioncount;

	@Expose
	@Getter
	private int count;

	@Expose
	@Getter
	private String classes;

	@Getter
	private CatchSnapshotInfo snapshotinfo;

	@Override
	public String[] childNames() {
		return new String[] { "snapshotinfo" };
	}

	@Override
	public String[] attributes() {
		return new String[] { "runtime", "exceptioncount", "count" };
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void setFieldByAttr(String fname, String fvalue) throws CrawlerConfigXmlException {
		if (RUNTIME.equals(fname)) {
			this.runtime = Integer.parseInt(fvalue);
		}
		if (EXCEPTIONCOUNT.equals(fname)) {
			this.exceptioncount = Integer.parseInt(fvalue);
		}
		if (COUNT.equals(fname)) {
			this.count = Integer.parseInt(fvalue);
		}
	}

	@Override
	protected void setFieldByNode(String fname, Node childNode) throws CrawlerConfigXmlException {

		if (SNAPSHOTINFO.equals(fname)) {
			childNode = this.valiChildNode(fname, childNode, CLASS);
			String classes = childNode.getAttributes().getNamedItem(CLASS).getNodeValue();

			this.classes = classes;

			try {
				Object obj = Class.forName(classes).newInstance();
				if (obj instanceof CatchSnapshotInfo) {
					this.snapshotinfo = (CatchSnapshotInfo) obj;
				} else {
					throw new CrawlerConfigXmlException("Element的class属性描述的类必须实现CatchSnapshotInfo接口。Element:%s class:%s CatchSnapshotInfo:%s",
							this.getTagname(), this.getClasses(), obj.toString());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

}
