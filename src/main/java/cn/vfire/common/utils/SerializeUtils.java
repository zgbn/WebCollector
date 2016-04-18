package cn.vfire.common.utils;

import java.lang.reflect.Type;
import java.security.MessageDigest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class SerializeUtils {

	/**
	 * 序列化
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> String serializeForJson(Object obj, Class<T> cls) {

		Gson gson = new Gson();

		Type type = new TypeToken<T>() {
		}.getType();

		return gson.toJson(obj);

	}

	@SuppressWarnings("unchecked")
	public static <T> T deserializeFromJson(String byteObj, Class<T> cls) {

		try {
			Gson gson = new Gson();

			Type type = new TypeToken<T>() {
			}.getType();

			Object obj = gson.fromJson(byteObj, cls);

			return (T) obj;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 计算字符串的MD5
	 * 
	 * @param s
	 * @return
	 */
	public final static String md5(String s) {

		if (s == null) {
			return null;
		}

		final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
