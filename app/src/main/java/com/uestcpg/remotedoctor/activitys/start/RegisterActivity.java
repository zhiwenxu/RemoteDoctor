package com.uestcpg.remotedoctor.activitys.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.BaseActivity;
import com.uestcpg.remotedoctor.beans.RegisterBean;
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
 * Created by POPLX on 2017/6/18.
 *
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{


    @InjectView(R.id.register_btn)
    Button rRegisterBtn;
    @InjectView(R.id.register_name)
    EditText rNameEdit;
    @InjectView(R.id.register_password)
    EditText rPasswordEdit;
    @InjectView(R.id.register_phone)
    EditText rPhoneEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private void init(){
        ButterKnife.inject(this);
        rRegisterBtn.setOnClickListener(this);
    }

    private void Register(){

        String pwd = rPasswordEdit.getText().toString();
        String phone = rPhoneEdit.getText().toString();
        String name = rNameEdit.getText().toString();

        if(StringUtil.isEmpty(phone)){
            T.show(this,getString(R.string.account_null_tip));
            return;
        }
        if(StringUtil.isEmpty(pwd)){
            T.show(this,getString(R.string.pwd_null_tip));
            return;
        }
        if(StringUtil.isEmpty(name)){
            T.show(this,getString(R.string.name_null_tip));
            return;
        }

        String pwdMD5 = MD5Util.stringMD5(pwd);
        ParamUtil.put("phone",phone);
        ParamUtil.put("password",pwdMD5);
        ParamUtil.put("name",name);
        ParamUtil.put("doctor","false");

        OkHttpManager.getInstance()._postAsyn(APPUrl.REGISTER_URL,ParamUtil.getParams()
                , new OkHttpCallBack() {
                    @Override
                    public void onRespone(String result) {
                        RegisterBean bean = GsonHelper.getGson().fromJson(result,RegisterBean.class);
                        T.show(RegisterActivity.this,bean.getMessage());
                        finish();
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }
                });

    }
    @Override
    public void onClick(View v) {
        if(v == rRegisterBtn){
            Register();
        }
    }
}
