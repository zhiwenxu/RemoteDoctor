package com.uestcpg.remotedoctor.utils;

/**
 * Created by dmsoft on 2017/6/19.
 */

public class StringUtil {

    public static boolean isTrue(String t){
        if(t == null || t.equals("") || t.equals("false")){
            return false;
        }
        if(t.equals("true")){
            return true;
        }
        return false;
    }
}
