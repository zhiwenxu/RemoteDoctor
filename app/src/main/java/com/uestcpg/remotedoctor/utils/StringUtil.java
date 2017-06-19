package com.uestcpg.remotedoctor.utils;

/**
 * Created by dmsoft on 2017/6/19.
 */

public class StringUtil {

    //判断字符串的值是否为true
    public static boolean isTrue(String t){

        if(isEmpty(t)){
            return false;
        }
        if(t.equals("true")){
            return true;
        }else{
            return false;
        }
    }

    //判断 字符串是否为空
    public static boolean isEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }
}
