package com.uestcpg.remotedoctor.activitys.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluemobi.dylan.photoview.library.PhotoView;

/**
 * Created by dmsoft on 2017/7/13.
 */

public class PhotoViewActivity extends BaseActivity implements View.OnClickListener{
    @InjectView(R.id.photo_view)
    PhotoView mPhotoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photoview);
        ButterKnife.inject(this);
        init();
    }

    private void init(){
        mPhotoView.setImageResource(getIntent().getIntExtra("pic",R.drawable.sick_record1));
        mPhotoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mPhotoView){
            finish();
        }
    }
}
