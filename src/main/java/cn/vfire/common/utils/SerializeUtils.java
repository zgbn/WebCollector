package cn.vfire.common.utils;

import java.lang.reflect.Type;

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
		}
		catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

}
