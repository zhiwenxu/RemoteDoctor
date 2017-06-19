package com.uestcpg.remotedoctor.activitys.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.main.MainActivity;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.app.BaseActivity;
import com.uestcpg.remotedoctor.beans.RegisterBean;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;
import com.uestcpg.remotedoctor.utils.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by POPLX on 2017/6/18.
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

        String pwd = MD5Util.stringMD5(rPasswordEdit.getText().toString());
        String phone = rPhoneEdit.getText().toString();
        String name = rNameEdit.getText().toString();
        JSONObject object = new JSONObject();
        try {
            object.put("phone",phone);
            object.put("password",pwd);
            object.put("name",name);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        OkHttpManager.getInstance()._postAsyn("http://doctor.xiaopeng.site:808/api/Register", "=" + object.toString(), new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                RegisterBean bean = GsonHelper.getGson().fromJson(result,RegisterBean.class);
                Toast.makeText(RegisterActivity.this,bean.getMessage(),Toast.LENGTH_SHORT).show();
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
