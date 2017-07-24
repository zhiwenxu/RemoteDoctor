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
    public static void setReservation_Name(Context context,String name){
        getSharedPreferencesResName(context).edit().putString(Constants.SHARE_RES_NAME,name).apply();
    }
    public static void setReservation_Sex(Context context,String sex){
        getSharedPreferencesResSex(context).edit().putString(Constants.SHARE_RES_SEX,sex).apply();
    }
    public static void setReservation_Old(Context context,String old){
        getSharedPreferencesResOld(context).edit().putString(Constants.SHARE_RES_OLD,old).apply();
    }
    public static void setReservation_Career(Context context,String name){
        getSharedPreferencesResCareer(context).edit().putString(Constants.SHARE_RES_CAREER,name).apply();
    }
    public static void setReservation_Height(Context context,String name){
        getSharedPreferencesResHeight(context).edit().putString(Constants.SHARE_RES_HEIGHT,name).apply();
    }
    public static void setReservation_Weight(Context context,String name){
        getSharedPreferencesResWeight(context).edit().putString(Constants.SHARE_RES_WEIGHT,name).apply();
    }

    public static String getUsername(Context context){
        return getSharedPreferences(context).getString(Constants.SHARE_USER_NAME,"");
    }
    public static String getPassWord(Context context){
        return getSharedPreferences(context).getString(Constants.SHARE_PASS_WORD,"");
    }
    public static String getReservation_Name(Context context){
        return getSharedPreferencesResName(context).getString(Constants.SHARE_RES_NAME,"");
    }
    public static String getReservation_Sex(Context context){
        return getSharedPreferencesResSex(context).getString(Constants.SHARE_RES_SEX,"");
    }
    public static String getReservation_Old(Context context){
        return getSharedPreferencesResOld(context).getString(Constants.SHARE_RES_OLD,"");
    }
    public static String getReservation_Career(Context context){
        return getSharedPreferencesResCareer(context).getString(Constants.SHARE_RES_CAREER,"");
    }
    public static String getReservation_Height(Context context){
        return getSharedPreferencesResHeight(context).getString(Constants.SHARE_RES_HEIGHT,"");
    }
    public static String getReservation_Weight(Context context){
        return getSharedPreferencesResWeight(context).getString(Constants.SHARE_RES_WEIGHT,"");
    }




    public static SharedPreferences getSharedPreferences(Context context){
        return   context.getSharedPreferences(Constants.SHARE_NAME,0);
    }

    public static SharedPreferences getSharedPreferencesResName(Context context){
        return   context.getSharedPreferences(Constants.SHARE_RES_NAME,0);
    }

    public static SharedPreferences getSharedPreferencesResSex(Context context){
        return   context.getSharedPreferences(Constants.SHARE_RES_SEX,0);
    }

    public static SharedPreferences getSharedPreferencesResOld(Context context){
        return   context.getSharedPreferences(Constants.SHARE_RES_OLD,0);
    }

    public static SharedPreferences getSharedPreferencesResCareer(Context context){
        return   context.getSharedPreferences(Constants.SHARE_RES_CAREER,0);
    }

    public static SharedPreferences getSharedPreferencesResHeight(Context context){
        return   context.getSharedPreferences(Constants.SHARE_RES_HEIGHT,0);
    }

    public static SharedPreferences getSharedPreferencesResWeight(Context context){
        return   context.getSharedPreferences(Constants.SHARE_RES_WEIGHT,0);
    }
}
