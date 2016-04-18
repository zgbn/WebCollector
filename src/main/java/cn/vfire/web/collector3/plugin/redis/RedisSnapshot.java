package cn.vfire.web.collector3.plugin.redis;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import cn.vfire.common.utils.SerializeUtils;
import cn.vfire.web.collector3.crawldb.Snapshot;
import cn.vfire.web.collector3.model.SnapshotInfo;

@Slf4j
public class RedisSnapshot implements Snapshot {

	private RedisDBConfig.DBName dbName;

	public RedisSnapshot(RedisDBConfig.DBName dbName) {
		this.dbName = dbName;
	}

	@Override
	public void saveSnapshotInfo(SnapshotInfo info) {

		String value = SerializeUtils.serializeForJson(info, SnapshotInfo.class);

		Jedis jedis = RedisDBConfig.getJedis();

		try {

			jedis.rpush(this.dbName.listSnapshotKey, value);

			log.debug("爬虫快照{}成功。", info);

		} catch (Exception e) {

			log.warn("Redis数据库保存快照信息失败。", e);

		} finally {

			if (jedis != null) {

				jedis.close();

			}
		}

	}

	@Override
	public List<SnapshotInfo> listSnapshotInfo() {

		Jedis jedis = RedisDBConfig.getJedis();

		List<SnapshotInfo> infos = new LinkedList<SnapshotInfo>();

		try {

			List<String> rs = jedis.lrange(this.dbName.listSnapshotKey, 0, -1);

			if (rs != null) {

				for (String info : rs) {
					infos.add(SerializeUtils.deserializeFromJson(info, SnapshotInfo.class));
				}

			}

			log.debug("获取爬虫快照列表成功。");

		} catch (Exception e) {

			log.warn("Redis数据库获取快照信息列表失败。", e);

		} finally {

			if (jedis != null) {

				jedis.close();

			}
		}

		return infos;

	}

}
