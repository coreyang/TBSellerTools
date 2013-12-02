package com.coreyang.test;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonLibTest {
	public static void main(String[] args) {
		String jsonString = "[[{\"f0\":\"nzk\",\"f1\":11729,\"f2\":32082,\"f3\":0.3749527159933424,\"f4\":19006,\"f5\":0.4421877017083351,\"f6\":0.5861181870917299,\"f7\":{\"word\":\"休闲裤 男\",\"num\":\"7096912\"},\"f8\":0.01810890564705289,\"f9\":5.6358},{\"f0\":\"nzk\",\"f1\":11729,\"f2\":32082,\"f3\":0.3749527159933424,\"f4\":19006,\"f5\":0.4421877017083351,\"f6\":0.5861181870917299,\"f7\":{\"word\":\"休闲裤 男\",\"num\":\"7096912\"},\"f8\":0.01810890564705289,\"f9\":5.6358}]]";
		JSONArray jsonarray = JSONArray.fromObject(jsonString);
		JSONArray jsonarray1 = JSONArray.fromObject(jsonarray.get(0));
		for(Object records: jsonarray1){
			JSONObject jb = JSONObject.fromObject(records);
			Iterator<String> iterator = jb.keys();
	        String key = null;
	        String value = null; 
	        while (iterator.hasNext())
	        {
	            key = iterator.next();
	            value = jb.getString(key);
	            if(key.equals("f7")){
	            	JSONObject jb1 = JSONObject.fromObject(value);
	            	System.out.println("f7:");
	            	System.out.println("word:"+jb1.get("word"));
	            	System.out.println("num:"+jb1.get("num"));
	            }else{
	            	System.out.println(key+":"+value);
	            }
	            
	        } 
		}
        /*JSONArray ja = jb.getJSONArray("data");
        List<Employee> empList = new ArrayList<Employee>();
        for (int i = 0; i < ja.size(); i++) {
        	String f0Str = ja.getJSONObject(i).getString("f0");
            System.out.println(f0Str);
        }*/
	}
}
