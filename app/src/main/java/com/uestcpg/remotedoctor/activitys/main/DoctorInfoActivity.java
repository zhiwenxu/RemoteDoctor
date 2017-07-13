package com.uestcpg.remotedoctor.activitys.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.app.BaseActivity;
import com.uestcpg.remotedoctor.beans.DoctorInfoBean;
import com.uestcpg.remotedoctor.beans.RespondBean;
import com.uestcpg.remotedoctor.network.APPUrl;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;
import com.uestcpg.remotedoctor.utils.ParamUtil;
import com.uestcpg.remotedoctor.utils.StringUtil;
import com.uestcpg.remotedoctor.utils.T;
import com.uestcpg.remotedoctor.views.library.AlertView;
import com.uestcpg.remotedoctor.views.library.OnConfirmeListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;

/**
 * Created by dmsoft on 2017/7/4.
 *
 */

public class DoctorInfoActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.doctor_icon)
    SimpleDraweeView mSimpleDraweeView;
    @InjectView(R.id.doctor_name)
    TextView mDoctorName;
    @InjectView(R.id.doctor_appellation)
    TextView mDoctorAppellation;
    @InjectView(R.id.doctor_major)
    TextView mDoctorMajor;
    @InjectView(R.id.doctor_send_btn)
    Button mSendBtn;
    @InjectView(R.id.yy_btn)
    Button mYyBtn;
    private String doctorPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        ButterKnife.inject(this);
        mSendBtn.setOnClickListener(this);
        mYyBtn.setOnClickListener(this);
        init();
    }

    private void init(){
        doctorPhone = getIntent().getStringExtra("doctorPhone");
        ParamUtil.put("token", AppStatus.getToken());
        ParamUtil.put("phone",doctorPhone);
        OkHttpManager.getInstance()._postAsyn(APPUrl.GET_DOCTOR_INFO_URL,ParamUtil.getParams(), new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                DoctorInfoBean bean = GsonHelper.getGson().fromJson(result,DoctorInfoBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    mSimpleDraweeView.setImageURI(bean.getIconUrl());
                    mDoctorName.setText(bean.getName());
                    mDoctorAppellation.setText(bean.getAppellation());
                    mDoctorMajor.setText(bean.getMajor());
                }else{
                    T.show(DoctorInfoActivity.this,bean.getMessage());
                }
            }
            @Override
            public void onError(Request request, Exception e) {
            }
        });
    }

    //发起预约
    private void orderYy(){
        AlertView alertView = new AlertView("预约时间",this, new OnConfirmeListener() {
            @Override
            public void result(String s) {
                ParamUtil.put("token", AppStatus.getToken());
                ParamUtil.put("sickPhone",AppStatus.getUserid());
                ParamUtil.put("doctorPhone",doctorPhone);
                ParamUtil.put("sickName",AppStatus.getUsername());
                ParamUtil.put("dateTime",s);
                OkHttpManager.getInstance()._postAsyn(APPUrl.ORDER_YY_URL,ParamUtil.getParams(), new OkHttpCallBack() {
                    @Override
                    public void onRespone(String result) {
                        Log.e("aa",result);
                        RespondBean bean = GsonHelper.getGson().fromJson(result,RespondBean.class);
                        T.show(DoctorInfoActivity.this,bean.getMessage());
                    }
                    @Override
                    public void onError(Request request, Exception e) {

                    }
                });
            }
        });
        alertView.show();
    }

    private void checkOrder(){

        ParamUtil.put("token",AppStatus.getToken());
        ParamUtil.put("sickPhone",AppStatus.getUserid());
        ParamUtil.put("doctorPhone",doctorPhone);
        OkHttpManager.getInstance()._postAsyn(APPUrl.CHECK_ORDER_URL, ParamUtil.getParams(), new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                RespondBean bean = GsonHelper.getGson().fromJson(result,RespondBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    RongIM.getInstance().startPrivateChat(DoctorInfoActivity.this,doctorPhone,"医生？");
                }
                else{
                    T.show(DoctorInfoActivity.this,bean.getMessage());
                }

            }

            @Override
            public void onError(Request request, Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == mSendBtn){
            checkOrder();
        }
        if(v == mYyBtn){
            orderYy();
        }
    }
}
