package com.uestcpg.remotedoctor.activitys.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.adapters.SickRecordAdapter;
import com.uestcpg.remotedoctor.app.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dmsoft on 2017/7/13.
 *
 */

public class SickRecordActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.sick_record_list)
    RecyclerView mRecyclerView;

    @InjectView(R.id.sick_record_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private SickRecordAdapter mSickRecordAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_record);
        ButterKnife.inject(this);
        init();
    }

    private void init(){
        initTitle();
        setLeftIm(R.drawable.left_arrow);
        setCenterTv(getString(R.string.sick_record));
        mSickRecordAdapter = new SickRecordAdapter(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,1));
        mRecyclerView.setAdapter(mSickRecordAdapter);
        mLeftIm.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == mLeftIm){
            finish();
        }
    }
}
