package cn.vfire.web.collector3.tools;

import java.lang.reflect.Field;
import java.util.List;

import cn.vfire.web.collector3.annotation.Label;
import cn.vfire.web.collector3.crawler.executor.Requester;
import cn.vfire.web.collector3.crawler.pool.TaskPool;
import cn.vfire.web.collector3.crawler.ware.CrawlerInfoWare;
import cn.vfire.web.collector3.crawler.ware.RequesterWare;
import cn.vfire.web.collector3.crawler.ware.TaskPoolWare;
import cn.vfire.web.collector3.model.CrawlerAttrInfo;
import cn.vfire.web.collector3.tools.crawler.element.CrawlerConfig;
import cn.vfire.web.collector3.tools.crawler.element.ProxyIP;

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
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
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


	public static final void setWareField(Object obj, CrawlerConfig config, TaskPool taskPool, Requester requester) {

		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {

			field.setAccessible(true);

			Object fieldValue = null;

			try {
				fieldValue = field.get(obj);
			}
			catch (Exception e) {
			}

			if ((fieldValue instanceof CrawlerInfoWare) && config != null) {

				CrawlerInfoWare value = (CrawlerInfoWare) fieldValue;

				value.setCrawlerAttrInfo(new CrawlerAttrInfo().formObj(config));

				ProxyIP proxyIp = new ProxyIP();
				List<ProxyIP> proxyIpList = config.getCrawlerConfigs().getProxyip();
				for (ProxyIP ip : proxyIpList) {
					proxyIp.setIp(ip);
				}
				value.setProxyIPs(proxyIp);
			}

			if ((fieldValue instanceof TaskPoolWare) && taskPool != null) {
				((TaskPoolWare) fieldValue).setTaskPool(taskPool);
			}

			if ((fieldValue instanceof RequesterWare) && requester != null) {
				((RequesterWare) fieldValue).setRequester(requester);
			}

		}

	}

}
