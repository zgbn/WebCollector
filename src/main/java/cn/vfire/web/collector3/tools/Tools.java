package cn.vfire.web.collector3.tools;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.vfire.web.collector3.annotation.Label;
import cn.vfire.web.collector3.crawler.executor.Requester;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.crawler.ware.RequesterWare;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector3.tools.crawler.proxyip.ProxyIpPool;

public class Tools {

	/**
	 * 一个特殊的toString方法，目的为了控制台输出特定的Obj信息。
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toStringByFieldLabel(Object obj) {

		if (obj == null) {
			return String.valueOf(obj);
		}

		Field[] fs = obj.getClass().getDeclaredFields();

		int length = fs == null ? 0 : fs.length;

		StringBuffer info = new StringBuffer();

		for (int i = 0; i < length; i++) {

			Field f = fs[i];

			f.setAccessible(true);

			String label = null, name = null, value = null;

			Label l = null;

			if ((l = f.getAnnotation(Label.class)) != null) {
				label = l.value();
			}

			name = f.getName();

			try {
				value = String.valueOf(f.get(obj));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			info.append(",").append(label).append(":").append(name).append("=").append(value);

		}

		if (info.length() > 0) {
			info.deleteCharAt(0);
		}

		info.insert(0, "{").insert(info.length(), "}");

		return info.toString();

	}

	public static final void setWareObj(Object obj, CrawlerConfig config, Requester requester) {

		if ((obj instanceof CrawlerInfoWare) && config != null) {

			CrawlerInfoWare value = (CrawlerInfoWare) obj;

			{
				value.setName(config.getId());
				value.setCrawlerAttrInfo(new CrawlerAttrInfo().formObj(config));
				value.setProxyIpPool(ProxyIpPool.getProxyIpPool());
				value.setSeedurl(config.getSeedurl());
			}

			if (config.getDatamode() != null) {
				value.setDataModes(config.getDatamode());
			}
			if (config.getSnapshot() != null) {
				value.setSnapshot(config.getSnapshot().getTime(), config.getSnapshot().getSize());
			}

			if (config.getRegexrules() != null) {
				value.setRegexRules(config.getRegexrules().getRegex());
			}

			if (config.getUnregexrules() != null) {
				value.setUnregexRules(config.getUnregexrules().getRegex());
			}

		}

		if ((obj instanceof RequesterWare) && requester != null) {
			((RequesterWare) obj).setRequester(requester);
		}

	}

	public static final void setWareField(Object obj, CrawlerConfig config, Requester requester) {

		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {

			field.setAccessible(true);

			Object fieldValue = null;

			try {
				fieldValue = field.get(obj);
			} catch (Exception e) {
			}

			setWareObj(fieldValue, config, requester);

		}

	}

	public static final boolean netPing(String ip) {
		try {
			InetAddress address = InetAddress.getByName(ip);
			return address.isReachable(5000);
		} catch (Exception e) {
			return false;
		}
	}

	public static final boolean netTelnet(String ip, int port) {
		Socket server = null;
		try {
			server = new Socket();
			InetSocketAddress address = new InetSocketAddress(ip, port);
			server.connect(address, 5000);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (server != null)
				try {
					server.close();
				} catch (IOException e) {
				}
		}
	}

	public static final String dateCurrentTime() {
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	public static enum TimeFormat {
		/** 天 */
		DD(24 * 60 * 60 * 1000d, "天"),
		/** 时 */
		HH(60 * 60 * 1000d, "时"),
		/** 分 */
		MI(60 * 1000d, "分"),
		/** 秒 */
		SS(1000d, "秒"),
		/** 毫秒 */
		SSS(1d, "毫秒"),

		AUTO();

		private double n;

		private String unit;

		private TimeFormat() {
		}

		private TimeFormat(double n, String unit) {
			this.n = n;
			this.unit = unit;
		}

		public String format(long time) {

			if (this == TimeFormat.AUTO) {

				if (time >= SSS.n && time < SS.n) {
					return String.format("%f%s", (time / SSS.n), SSS.unit);
				}

				if (time >= SS.n && time < MI.n) {
					return String.format("%f%s", (time / SS.n), SS.unit);
				}

				if (time >= MI.n && time < HH.n) {
					return String.format("%f%s", (time / MI.n), MI.unit);
				}

				if (time >= HH.n && time < DD.n) {
					return String.format("%f%s", (time / HH.n), HH.unit);
				}

				if (time >= DD.n) {
					return String.format("%f%s", (time / DD.n), DD.unit);
				}

				return String.format("%f%s", (time / SSS.n), SSS.unit);

			} else {

				return String.format("%f%s", (time / this.n), this.unit);

			}

		}

	}

	public static void main(String[] args) {
		String ip = "223.152.208.100";
		int port = 8080;
		long t1 = System.currentTimeMillis();
		netPing(ip);
		long t2 = System.currentTimeMillis();
		netTelnet(ip, port);
		long t3 = System.currentTimeMillis();

		System.out.println(t2 - t1);
		System.out.println(t3 - t2);
	}
}
