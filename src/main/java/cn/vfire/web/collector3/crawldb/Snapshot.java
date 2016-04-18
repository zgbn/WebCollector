package cn.vfire.web.collector3.crawldb;

import java.util.List;

import cn.vfire.web.collector3.model.SnapshotInfo;

/**
 * 快照信息
 * 
 * @author ChenGang
 *
 */
public interface Snapshot {

	/**
	 * 保存一个新的快照
	 * 
	 * @param info
	 */
	public void saveSnapshotInfo(SnapshotInfo info);

	/**
	 * 列出所有的快照信息
	 * 
	 * @return
	 */
	public List<SnapshotInfo> listSnapshotInfo();

}
