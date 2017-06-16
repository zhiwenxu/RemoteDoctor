package com.uestcpg.remotedoctor.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.uestcpg.remotedoctor.R;

/**
 * Created by dmsoft on 2017/6/14.
 */

public class BaseFragmentActivity extends FragmentActivity {

    protected TextView mLeftTv,mCenterTv,mRightTv;
    protected ImageView mLeftIm,mRightIm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BaseApplication.addActivity(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BaseApplication.removeActivity(this);
    }
    //初始化标题
    protected void initTitle(){
        mLeftTv = (TextView)findViewById(R.id.title_left_tv);
        mCenterTv = (TextView)findViewById(R.id.title_center_tv);
        mRightTv = (TextView)findViewById(R.id.title_right_tv);
        mLeftIm = (ImageView)findViewById(R.id.title_back);
        mRightIm = (ImageView)findViewById(R.id.title_right_im);
    }
    //设置左边文字
    protected void setLeftTv(String left){
        setTv(mLeftTv,left);
    }
    //设置标题文字
    protected void setCenterTv(String center){
        setTv(mCenterTv,center);
    }

    //设置右边文字
    protected void setRightTv(String right){
        setTv(mRightTv,right);
    }
    //设置左边返回图形
    protected void setLeftIm(int bg){
        setIm(mLeftIm,bg);
    }
    //设置右边图形
    protected void setRightIm(int bg){
        setIm(mRightIm,bg);
    }
    //通用方法设置textview
    private void setTv(TextView textView,String text){
        if(textView != null){
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }
    //设置标题图形的背景颜色
    private void setIm(ImageView im,int bg){
        if(im != null){
            im.setVisibility(View.VISIBLE);
            im.setBackgroundResource(bg);
        }
    }
}
