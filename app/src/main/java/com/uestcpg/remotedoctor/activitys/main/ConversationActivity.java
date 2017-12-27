package com.uestcpg.remotedoctor.activitys.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.app.BaseFragmentActivity;
import com.uestcpg.remotedoctor.beans.DoctorInfoBean;
import com.uestcpg.remotedoctor.network.APPUrl;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;
import com.uestcpg.remotedoctor.utils.ParamUtil;
import com.uestcpg.remotedoctor.utils.StringUtil;

/**
 * Created by dmsoft on 2017/6/14.
 *
 */

public class ConversationActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initViews();
    }
    private void initViews(){
        initTitle();
        String phone = getIntent().getData().getQueryParameter("targetId");
        AppStatus.setTargetId(phone);
        ParamUtil.put("phone",phone);
        OkHttpManager.getInstance()._postAsyn(APPUrl.GET_DOCTOR_INFO_URL, ParamUtil.getParams(), new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                DoctorInfoBean bean = GsonHelper.getGson().fromJson(result,DoctorInfoBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    setCenterTv(bean.getName());
                }
            }

            @Override
            public void onError(Request request, Exception e) {

            }
        });
    }
}
