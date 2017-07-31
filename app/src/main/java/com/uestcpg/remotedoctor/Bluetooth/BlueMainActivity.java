package com.uestcpg.remotedoctor.Bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewAnimator;
import com.uestcpg.remotedoctor.app.BaseActivity;

import com.uestcpg.remotedoctor.Bluetooth.common.activities.SampleActivityBase;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.see_how.LaunchActivity;
import com.uestcpg.remotedoctor.Bluetooth.common.logger.LogWrapper;
import com.uestcpg.remotedoctor.Bluetooth.common.logger.Log;
import com.uestcpg.remotedoctor.Bluetooth.common.logger.LogFragment;
import com.uestcpg.remotedoctor.Bluetooth.common.logger.MessageOnlyLogFilter;


import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by poplx on 2017/7/20.
 */

public class BlueMainActivity extends SampleActivityBase implements View.OnClickListener{
    @InjectView(R.id.see_how)//ABC
            Button See_How;//ABC

    public static final String TAG = "BlueMainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//ABC
        setContentView(R.layout.activity_bluetooth);//ABC
        ButterKnife.inject(this);//ABC
        See_How.setOnClickListener(this);//ABC

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}
