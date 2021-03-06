package com.uestcpg.remotedoctor.activitys.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class OrderActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    @InjectView(R.id.order_list)
    ListView mListView;

    private OrderListAdapter mOrderListAdapter;
    private List<Order> orders = new ArrayList<>();

    private StringUtil isaccept;
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
        mListView.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(StringUtil.isEmpty(orders.get(position).getIsAccept())){
            return;
        }
       if(!StringUtil.isTrue(orders.get(position).getIsAccept()))
       {
           String reason = orders.get(position).getReason();

           new AlertDialog.Builder(OrderActivity.this).setTitle(R.string.reject_reason)
                   .setMessage(reason)
                   .setPositiveButton(R.string.dialog_comfire, null)
                   .show();
       }
    }
}
