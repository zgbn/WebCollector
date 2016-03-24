package cn.vfire.common.utils.json;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DefaultJson {

	private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().enableComplexMapKeySerialization()
			.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create();

	private Object json;


	public String toJson(Object obj) {

		if (this.json instanceof Gson) {
			Gson gson = (Gson) json;
			return gson.toJson(obj);
		}
		else if (this.json instanceof JSONObject) {
			JSONObject orgJson = (JSONObject) json;
			return orgJson.toString();
		}
		else {
			return gson.toJson(obj);
		}

	}


	public void setJson(Object json) {

		this.json = json;

	}

}
