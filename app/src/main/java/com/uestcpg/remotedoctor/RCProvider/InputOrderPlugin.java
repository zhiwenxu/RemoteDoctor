package com.uestcpg.remotedoctor.RCProvider;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.utils.T;
import com.uestcpg.remotedoctor.views.library.AlertView;
import com.uestcpg.remotedoctor.views.library.OnConfirmeListener;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by dmsoft on 2017/6/30.
 */

public class InputOrderPlugin implements IPluginModule {
    private Context context;
    @Override
    public Drawable obtainDrawable(Context context) {
        this.context = context;
        return ContextCompat.getDrawable(context,R.drawable.input_order_bg);
    }

    @Override
    public String obtainTitle(Context context) {
        return "预约";
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        AlertView alertView = new AlertView("预约时间", context, new OnConfirmeListener() {
            @Override
            public void result(String s) {
                T.show(context,s);
            }
        });
        alertView.show();
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
