package com.mhsoft.emr.util;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonUtil {

	public static String toJSONValue(String name,String json){
		String result = "{\"result\":[{\""+name+"\":\""+json+"\"}]}";
		return result;
	}
	
	public static String toJSONArray(String json){
		String result = "{\"result\":"+json+"}";
		return result;
	}
	
	public static String toJSONObject(String json){
		String result = "{\"result\":["+json+"]}";
		return result;
	}
	
	public static String toJSONEmpty(){
		String result = "{\"result\":[{}]}";
		return result;
	}
	
	public static String success(boolean isSuccess,Object data,String msg){
		String json = null;
		if (data instanceof List) {
			List list = (List) data;
			json = JsonUtil.toJSONArray(JSONArray.toJSONString(list));
		}else if(data instanceof Map){
			Map map = (Map) data;
			json = JsonUtil.toJSONObject(JSONObject.toJSONString(map));
		}else if(data instanceof String[]){
			String[] values = (String[]) data;
			json = JsonUtil.toJSONValue(values[0], values[1]);
		}
		
		String result = "{success:true";
		if(!isSuccess){
			result = "{success:false";
		}
		if(null!=json){
			result += ","+json.substring(1,json.length()-1);
		}
		if(null!=msg){
			result += ",msg:\""+msg+"\"";
		}
		result += "}";
		return result;
	}
}
