package cn.vfire.web.collector3.tools;

import java.lang.reflect.Field;
import java.util.List;

import cn.vfire.web.collector3.annotation.Label;
import cn.vfire.web.collector3.model.Page;
import cn.vfire.web.collector3.model.ResultData;
import cn.vfire.web.collector3.tools.crawler.element.NList;
import cn.vfire.web.collector3.tools.crawler.element.NNode;

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

			info.append(",").append(label).append(":").append(name).append(":").append(value);

		}

		if (info.length() > 0) {
			info.deleteCharAt(0);
		}

		return info.toString();

	}



}
