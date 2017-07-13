package com.uestcpg.remotedoctor.fragments.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.app.BaseActivity;
import com.uestcpg.remotedoctor.beans.RegisterBean;
import com.uestcpg.remotedoctor.beans.RespondBean;
import com.uestcpg.remotedoctor.network.APPUrl;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;
import com.uestcpg.remotedoctor.utils.MD5Util;
import com.uestcpg.remotedoctor.utils.ParamUtil;
import com.uestcpg.remotedoctor.utils.StringUtil;
import com.uestcpg.remotedoctor.utils.T;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by poplx on 2017/6/29.
 *
 */

public class MeFragmentChange extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.confirm_change_btn)
    Button Confirm_Change_btn;
    @InjectView(R.id.change_password)
    EditText Change_Password;
    @InjectView(R.id.confirm_password)
    EditText Confirm_Password;
    @InjectView(R.id.change_name)
    EditText Change_Name;
    @InjectView(R.id.change_description)
    EditText Change_desctiption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_change);
        init();
    }

    private void init(){
        ButterKnife.inject(this);
        initTitle();
        setCenterTv("个人资料修改");
        setLeftIm(R.drawable.left_arrow);
        Change_Name.setText(getIntent().getStringExtra("name"));
        Change_desctiption.setText(getIntent().getStringExtra("description"));
        Confirm_Change_btn.setOnClickListener(this);
        mLeftIm.setOnClickListener(this);
    }

    private void ConfirmChange(){
        String pwd = MD5Util.stringMD5(Change_Password.getText().toString());
        String confirm_pwd = MD5Util.stringMD5(Confirm_Password.getText().toString());
        String name = Change_Name.getText().toString();
        String description = Change_desctiption.getText().toString();

        if(StringUtil.isEmpty(name)){
            T.show(this,getString(R.string.account_null_tip));
            return;
        }
        if(StringUtil.isEmpty(pwd)){
            T.show(this,getString(R.string.pwd_null_tip));
            return;
        }
        if(StringUtil.isEmpty(confirm_pwd)){
            T.show(this,getString(R.string.confirm_pwd_null_tip));
            return;
        }
        if(!pwd.equals(confirm_pwd)){
            T.show(this,getString(R.string.wrong_pwd_tip));
            return;
        }
        ParamUtil.put("token",AppStatus.getToken());
        ParamUtil.put("phone",AppStatus.getUserid());
        ParamUtil.put("password",pwd);
        ParamUtil.put("name",name);
        ParamUtil.put("description",description);

        OkHttpManager.getInstance()._postAsyn(APPUrl.SET_SICK_INFO_URL,ParamUtil.getParams(), new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                RespondBean bean = GsonHelper.getGson().fromJson(result, RespondBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    finish();
                }else{
                    T.show(MeFragmentChange.this,bean.getMessage());
                }
            }
            @Override
            public void onError(Request request, Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == Confirm_Change_btn){
            ConfirmChange();
        }
        if(v == mLeftIm){
            finish();
        }
    }

}
