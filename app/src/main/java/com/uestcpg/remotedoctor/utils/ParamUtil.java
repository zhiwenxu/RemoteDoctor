package com.uestcpg.remotedoctor.utils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    public static Map<String,String> getParams(){
        Map<String,String> m = new HashMap<>();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next().toString();
            m.put(key,map.get(key).toString());
        }
        map.clear();
        return m;
    }
}
