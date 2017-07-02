package com.uestcpg.remotedoctor.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dmsoft on 2017/6/30.
 */

public class SPUtil {

    public static void setUsername(Context context,String username){
        getSharedPreferences(context).edit().putString(Constants.SHARE_USER_NAME,username).apply();
    }
    public static void setPassword(Context context,String password){
        getSharedPreferences(context).edit().putString(Constants.SHARE_PASS_WORD,password).apply();
    }

    public static String getUsername(Context context){
        return getSharedPreferences(context).getString(Constants.SHARE_USER_NAME,"");
    }
    public static String getPassWord(Context context){
        return getSharedPreferences(context).getString(Constants.SHARE_PASS_WORD,"");
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return   context.getSharedPreferences(Constants.SHARE_NAME,0);
    }
}
