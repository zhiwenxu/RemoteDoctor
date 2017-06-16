package com.uestcpg.remotedoctor.network;

import com.squareup.okhttp.Request;

/**
 * Created by dmsoft on 2017/6/16.
 */

public interface OkHttpCallBack {
    void onRespone(String result);
    void onError(Request request,Exception e);
}
