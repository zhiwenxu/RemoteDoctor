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
import com.uestcpg.remotedoctor.beans.LoginBean;
import com.uestcpg.remotedoctor.beans.RCBean;
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
import io.rong.imageloader.utils.L;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by xuzhiwen on 2017/6/14.
 * 登录页面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.login_btn)
    Button mLoginBtn;
    @InjectView(R.id.login_register_btn)
    Button mLoginRegisterBtn;
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
        mLoginRegisterBtn.setOnClickListener(this);
    }
    private void checkLogin(){
        String pwd = mPasswordEdit.getText().toString();
        String phone = mPhoneEdit.getText().toString();
        if(StringUtil.isEmpty(phone)){
            T.show(this,getString(R.string.account_null_tip));
            return;
        }
        if(StringUtil.isEmpty(pwd)){
            T.show(this,getString(R.string.pwd_null_tip));
            return;
        }
        String pwdMD5 = MD5Util.stringMD5(pwd);
        ParamUtil.put("phone",phone);
        ParamUtil.put("password",pwdMD5);
        OkHttpManager.getInstance()._postAsyn(APPUrl.LOGIN_URL,ParamUtil.getParams(), new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                LoginBean bean = GsonHelper.getGson().fromJson(result,LoginBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    AppStatus.setToken(bean.getToken());
                    getRCToken(bean.getToken());
                }
                else{
                    T.show(LoginActivity.this,getString(R.string.account_pwd_null_tip));
                }
                connect(bean.getToken());
            }
            @Override
            public void onError(Request request, Exception e) {

            }
        });
    }

    private void getRCToken(String token){
        OkHttpManager.getInstance()._getAsyn(APPUrl.GET_RCTOKEN_URL, token, new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                Log.e("e",result+"|");
                RCBean bean = GsonHelper.getGson().fromJson(result,RCBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    AppStatus.setrCToken(bean.getRCToken());
                    connect(bean.getRCToken());
                }else{
                    T.show(LoginActivity.this,bean.getMessage());
                }
            }
            @Override
            public void onError(Request request, Exception e) {
                T.show(LoginActivity.this,getString(R.string.get_RC_error));
            }
        });
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

    private void Register(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        if(v == mLoginBtn){
            checkLogin();
        }else if(v == mLoginRegisterBtn){
            Register();
        }
    }
}
