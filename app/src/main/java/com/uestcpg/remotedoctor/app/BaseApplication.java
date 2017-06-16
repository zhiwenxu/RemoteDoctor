package com.uestcpg.remotedoctor.app;

import android.app.Application;

import io.rong.imkit.RongIM;

/**
 * Created by dmsoft on 2017/6/13.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
    }
}
