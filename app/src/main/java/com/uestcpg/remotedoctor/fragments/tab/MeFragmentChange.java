package com.uestcpg.remotedoctor.fragments.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.BaseActivity;
import com.uestcpg.remotedoctor.utils.MD5Util;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by poplx on 2017/6/29.
 */

public class MeFragmentChange extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.confirm_change_btn)
    Button Confirm_Change_btn;
    @InjectView(R.id.change_password)
    EditText Change_Password;
    @InjectView(R.id.change_name)
    EditText Change_Name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_me_change);
        init();
    }

    private void init(){
        ButterKnife.inject(this);
        Confirm_Change_btn.setOnClickListener(this);
    }

    private void ConfirmChange(){
        String pwd = MD5Util.stringMD5(Change_Password.getText().toString());
        String name = Change_Name.getText().toString();
    }

    @Override
    public void onClick(View v) {
        if(v == Confirm_Change_btn){
            ConfirmChange();
        }
    }

}
