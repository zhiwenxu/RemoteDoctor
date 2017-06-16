package com.uestcpg.remotedoctor.app;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by dmsoft on 2017/6/13.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;
    private static List<Activity> activities = new ArrayList<>();
    private static List<FragmentActivity> fragmentActivities = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RongIM.init(this);
    }


    //获得application实例
    public static BaseApplication getInstance(){
        return instance;
    }

    //添加fragmentactivity到list中
    public void addFragmentActivity(FragmentActivity activity){
        if(!fragmentActivities.contains(activity)){
            fragmentActivities.add(activity);
        }
    }

    //添加activity到集合中，用于管理和退出
    public void addActivity(Activity activity){
        if(!activities.contains(activity)){
            activities.add(activity);
        }
    }
    //移除单个activity
    public void removeActivity(Activity activity){
        if(activities.contains(activity)){
            activities.remove(activity);
        }
    }
    //移除当个fragmentActivity
    public void removeFragmentActivity(FragmentActivity fragmentActivity){
        if(activities.contains(fragmentActivity)){
            fragmentActivities.remove(fragmentActivity);
        }
    }

    //移除全部acitivity
    private static void ActivityExit(){
        for(Activity activity : activities){
            activity.finish();
        }
        for(int i=activities.size()-1;i>=0;i--){
            activities.remove(i);
        }
    }
    //移除全部fragmentactivity
    private static void FragmentActivityExit(){
        for(FragmentActivity activity : fragmentActivities){
            activity.finish();
        }
        for(int i=fragmentActivities.size()-1;i>=0;i--){
            fragmentActivities.remove(i);
        }
    }

    //对出应用
    public static void exit(){
        ActivityExit();
        FragmentActivityExit();
    }


}
