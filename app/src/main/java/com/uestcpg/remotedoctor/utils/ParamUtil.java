package com.uestcpg.remotedoctor.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by xzw on 2017/6/19.
 * 将参数转换成json格式
 */

public class ParamUtil {

    private static final Map<String,Object> map = new HashMap<>();

    //将参数添加到map中
    public static void put(String key,Object value){
        map.put(key,value);
    }
    //获得参数的json格式
    public static String getParams(){
        Set set = map.keySet();
        Iterator i = set.iterator();
        JSONObject jsonObject = new JSONObject();
        while (i.hasNext()){
            String key = i.next().toString();
            try {
                jsonObject.put(key,map.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        map.clear();//及时清空map中的内容
        return "=" + jsonObject.toString();
    }
}
