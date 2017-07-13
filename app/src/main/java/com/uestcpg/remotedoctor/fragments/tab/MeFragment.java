package com.uestcpg.remotedoctor.fragments.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.main.OrderActivity;
import com.uestcpg.remotedoctor.activitys.main.SickRecordActivity;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.beans.SickInfoBean;
import com.uestcpg.remotedoctor.network.APPUrl;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;
import com.uestcpg.remotedoctor.utils.ParamUtil;
import com.uestcpg.remotedoctor.utils.StringUtil;
import com.uestcpg.remotedoctor.utils.T;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dmsoft on 2017/6/14.
 */

public class MeFragment extends Fragment implements View.OnClickListener{


    @InjectView(R.id.me_icon)
    SimpleDraweeView mSinmpleDraweeView;
    @InjectView(R.id.me_name)
    TextView mNameTv;
    @InjectView(R.id.me_description)
    TextView mDescription;
    @InjectView(R.id.sick_info_layout)
    RelativeLayout mSickInfoLayout;
    @InjectView(R.id.order_layout)
    RelativeLayout mOrderLayout;
    @InjectView(R.id.sick_record_layout)
    RelativeLayout mSickRecordLayout;

    public static MeFragment getInstance(){
        return new MeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_me,null);
        ButterKnife.inject(this,contentView);
        init();
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getInfo();
    }

    private void getInfo(){
        ParamUtil.put("token", AppStatus.getToken());
        ParamUtil.put("phone",AppStatus.getUserid());
        OkHttpManager.getInstance()._postAsyn(APPUrl.SICK_INFO_URL, ParamUtil.getParams(),new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                SickInfoBean bean = GsonHelper.getGson().fromJson(result,SickInfoBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    AppStatus.setUsername(bean.getName());
                    mSinmpleDraweeView.setImageURI(bean.getIconUrl());
                    mNameTv.setText(bean.getName());
                    mDescription.setText(bean.getDesception());
                }else{
                    T.show(getActivity(),bean.getMessage());
                }
            }
            @Override
            public void onError(Request request, Exception e) {

            }
        });
    }

    private void init(){
        mSickInfoLayout.setOnClickListener(this);
        mOrderLayout.setOnClickListener(this);
        mSickRecordLayout.setOnClickListener(this);
    }

    private void ChangeInfomation(){
        Intent intent = new Intent(getActivity(), MeFragmentChange.class);
        intent.putExtra("name",mNameTv.getText().toString());
        intent.putExtra("description",mDescription.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == mSickInfoLayout){
            ChangeInfomation();
        }
        if(v == mOrderLayout){
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            startActivity(intent);
        }
        if(v == mSickRecordLayout){
            Intent intent = new Intent(getActivity(), SickRecordActivity.class);
            startActivity(intent);
        }
    }

}
