package com.uestcpg.remotedoctor.activitys.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.main.MainActivity;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.app.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by POPLX on 2017/6/18.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    public static final String Token1 = "D4tAyYbSy0+RNfvmPjwk0H5S8BrvE5BcWVpMTzvyoQNRMSNrTJmkI+A3tRftYXOKxvW9g3fCFy//hJn7hCOg3Npw2ALKUrh6";
    public static final String Token2 = "twAqWo17/NEvWFdbFTEz635S8BrvE5BcWVpMTzvyoQNRMSNrTJmkI2vPDSzN4KLcRuyesJSoYYXGVBFJTQVu3A==";

    public static final String id1 = "xuzhiwen";
    public static final String id2 = "xiaoyu";

    @InjectView(R.id.register_btn)
    Button rRegisterBtn;
    @InjectView(R.id.register_name)
    EditText rNameEdit;
    @InjectView(R.id.register_password)
    EditText rPasswordEdit;
    @InjectView(R.id.register_phone)
    EditText rPhoneEdit;

    String pwd = rPasswordEdit.getText().toString();
    String phone = rPhoneEdit.getText().toString();
    String name = rNameEdit.getText().toString();

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
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v) {
        if(v == rRegisterBtn){
            Register();
        }
    }
}
