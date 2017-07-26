package com.uestcpg.remotedoctor.RCProvider;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.Bluetooth.BlueMainActivity;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.beans.RespondBean;
import com.uestcpg.remotedoctor.network.APPUrl;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;
import com.uestcpg.remotedoctor.utils.CalenderUtil;
import com.uestcpg.remotedoctor.utils.ParamUtil;
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
        return "脉搏治疗";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        context.startActivity(new Intent(context,BlueMainActivity.class));
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
