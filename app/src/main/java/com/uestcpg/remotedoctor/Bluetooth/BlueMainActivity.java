package com.uestcpg.remotedoctor.Bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.see_how.LaunchActivity;
import com.uestcpg.remotedoctor.app.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by poplx on 2017/7/20.
 */

public class BlueMainActivity extends BaseActivity implements View.OnClickListener{
    @InjectView(R.id.see_how)//ABC
            Button See_How;//ABC

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//ABC
        setContentView(R.layout.activity_bluetooth);//ABC
        ButterKnife.inject(this);//ABC
        See_How.setOnClickListener(this);//ABC
    }



    private void how(){
        Intent intent = new Intent(BlueMainActivity.this, LaunchActivity.class);//ABC
        startActivity(intent);//ABC
    }//ABC
    @Override
    public void onClick(View v) {//ABC
        if(v == See_How){//ABC
            how();//ABC
        }//ABC
    }
}
