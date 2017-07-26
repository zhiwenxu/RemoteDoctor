package com.uestcpg.remotedoctor.activitys.see_how;

/**
 * Created by poplx on 2017/7/25.
 */

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.uestcpg.remotedoctor.Bluetooth.BlueMainActivity;
import com.uestcpg.remotedoctor.R;

import java.util.ArrayList;

public class LaunchActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private ArrayList<View> viwePagerItems;
    private Button btnStart;
    private IndicatorView indicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initView();
        initData();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.view1, null);
        View view2 = inflater.inflate(R.layout.view2, null);
        //View view3 = inflater.inflate(R.layout.view3, null);
        btnStart = (Button) view2.findViewById(R.id.startBtn);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viwePagerItems = new ArrayList<View>();
        viwePagerItems.add(view1);
        viwePagerItems.add(view2);
        //viwePagerItems.add(view3);

        indicatorView = (IndicatorView)findViewById(R.id.indicatorView);
        indicatorView.setIndicatorCount(viwePagerItems.size());
    }

    private void initData() {
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(viwePagerItems);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(vpAdapter);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startbutton();
            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        indicatorView.setCurIndicatorIndex(arg0);
    }

    private void startbutton() {
        Intent intent = new Intent();
        intent.setClass(LaunchActivity.this, BlueMainActivity.class);
        startActivity(intent);
        this.finish();
    }

    class ViewPagerAdapter extends PagerAdapter {

        private ArrayList<View> mViewList;
        public ViewPagerAdapter (ArrayList<View> views){
            this.mViewList = views;
        }

        @Override
        public int getCount() {
            if (mViewList != null) {
                return mViewList.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(View view, int position) {

            ((ViewPager) view).addView(mViewList.get(position), 0);

            return mViewList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object arg1) {
            return (view == arg1);
        }

        @Override
        public void destroyItem(View view, int position, Object arg2) {
            ((ViewPager) view).removeView(mViewList.get(position));
        }
    }

}