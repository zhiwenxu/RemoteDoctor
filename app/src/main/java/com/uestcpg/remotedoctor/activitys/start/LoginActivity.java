package com.uestcpg.remotedoctor.activitys.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.main.MainActivity;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.app.BaseActivity;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by xuzhiwen on 2017/6/14.
 * 登录页面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{


    public static final String Token1 = "D4tAyYbSy0+RNfvmPjwk0H5S8BrvE5BcWVpMTzvyoQNRMSNrTJmkI+A3tRftYXOKxvW9g3fCFy//hJn7hCOg3Npw2ALKUrh6";
    public static final String Token2 = "twAqWo17/NEvWFdbFTEz635S8BrvE5BcWVpMTzvyoQNRMSNrTJmkI2vPDSzN4KLcRuyesJSoYYXGVBFJTQVu3A==";

    public static final String id1 = "xuzhiwen";
    public static final String id2 = "xiaoyu";

    @InjectView(R.id.login_btn)
    Button mLoginBtn;
    @InjectView(R.id.login_phone)
    EditText mPhoneEdit;
    @InjectView(R.id.login_password)
    EditText mPasswordEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init(){
        ButterKnife.inject(this);
        mLoginBtn.setOnClickListener(this);
    }
    private void checkLogin(){
        String pwd = mPasswordEdit.getText().toString();
        String phone = mPhoneEdit.getText().toString();
        if(!pwd.equals("123456")){
            Toast.makeText(this,"账号或密码错误",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!phone.equals(id1) && !phone.equals(id2)){
            Toast.makeText(this,"账号或密码错误",Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.equals(id1)){
            setAppStatus(Token1,id1,id2);
            connect(Token1);
        }else{
            setAppStatus(Token2,id2,id1);
            connect(Token2);
        }
    }

    private void setAppStatus(String token,String id1,String id2){
        AppStatus.setTagetId(id2);
        AppStatus.setToken(token);
        AppStatus.setUserId(id1);
        AppStatus.setUsername(id1);
    }

    private void connect(String token) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {

            }
            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == mLoginBtn){
            checkLogin();
        }
    }
}
