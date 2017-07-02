package com.uestcpg.remotedoctor.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by dmsoft on 2017/6/16.
 */

public class OkHttpManager {

    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static OkHttpManager mInstance;
    private OkHttpClient okHttpClient ;
    private Handler handler;

    public static OkHttpManager getInstance(){
        if(mInstance == null){
            synchronized (OkHttpManager.class){
                mInstance = new OkHttpManager();
            }
        }
        return mInstance;
    }

    private OkHttpManager(){
        handler = new Handler(Looper.getMainLooper());
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(20,TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(20,TimeUnit.SECONDS);
    }
    //设置缓存
    public OkHttpManager setCache(Context context){
        File cacheFile = context.getExternalCacheDir();
        int cacheSize = 10*1024*1024;
        okHttpClient.setCache(new Cache(cacheFile.getAbsoluteFile(),cacheSize));
        return mInstance;
    }

    //get方式异步请求
    public void _getAsyn(String url,final OkHttpCallBack callBack){
        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.url(url).build();
        Call call = okHttpClient.newCall(request);
        callRequest(call,callBack);
    }

    //post方式异步请求
    public void _postAsyn(String url, Map<String,String> map, final OkHttpCallBack callBack){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if(map != null){
            Set<String> set = map.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
                String key = iterator.next().toString();
                builder.add(key,map.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Call call = okHttpClient.newCall(request);
        callRequest(call,callBack);
    }

    public void callRequest(final Call call, final OkHttpCallBack callBack){
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallback(request,e,callBack);
            }
            @Override
            public void onResponse(Response response) throws IOException {
                sendSuccessCallback(response,callBack);
            }
        });
    }

    //请求成功后回调，在ui线程中执行回调
    private void sendSuccessCallback(Response response, final OkHttpCallBack callBack) throws IOException {
        final String result = response.body().string();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBack != null){
                    callBack.onRespone(result);
                }
            }
        });
    }

    //请求失败后回调，在ui现在中
    private void sendFailCallback(final Request request, final Exception e, final OkHttpCallBack callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBack != null){
                    callBack.onError(request,e);
                }
            }
        });
    }
}
