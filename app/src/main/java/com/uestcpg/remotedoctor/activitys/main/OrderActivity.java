package com.uestcpg.remotedoctor.activitys.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.Class.Order;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.adapters.OrderListAdapter;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.app.BaseActivity;
import com.uestcpg.remotedoctor.beans.OrderBean;
import com.uestcpg.remotedoctor.network.APPUrl;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;
import com.uestcpg.remotedoctor.utils.ParamUtil;
import com.uestcpg.remotedoctor.utils.StringUtil;
import com.uestcpg.remotedoctor.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dmsoft on 2017/7/5.
 *
 */

public class OrderActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.order_list)
    ListView mListView;

    private OrderListAdapter mOrderListAdapter;
    private List<Order> orders = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.inject(this);
        init();
    }
    private void init(){

        initTitle();
        setCenterTv(getResources().getString(R.string.my_rc));
        setLeftIm(R.drawable.left_arrow);
        mLeftIm.setOnClickListener(this);

        mOrderListAdapter = new OrderListAdapter(this,orders);
        mListView.setAdapter(mOrderListAdapter);

        ParamUtil.put("token", AppStatus.getToken());
        ParamUtil.put("phone",AppStatus.getUserid());
        OkHttpManager.getInstance()._postAsyn(APPUrl.GET_OREDER_URL,ParamUtil.getParams(), new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                OrderBean bean = GsonHelper.getGson().fromJson(result,OrderBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    mOrderListAdapter.addDatas(bean.getOrders());
                }else{
                    T.show(OrderActivity.this,bean.getMessage());
                }
            }
            @Override
            public void onError(Request request, Exception e) {

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
