package com.uestcpg.remotedoctor.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uestcpg.remotedoctor.Class.Order;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.main.SeeReservationInfoActivity;
import com.uestcpg.remotedoctor.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;


/**
 * Created by dmsoft on 2017/7/5.
 */

public class OrderListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Order> orders;

    public OrderListAdapter(Context context,List<Order> orders){
        this.mContext = context;
        this.orders = orders;

    }
    //添加所有数据
    public void addDatas(List<Order> datas){
        orders.addAll(datas);
        notifyDataSetChanged();
    }
    //添加单一数据
    public void addData(Order doctor){
        orders.add(doctor);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_list_item,null);
            viewHolder.iconImage = (SimpleDraweeView)convertView.findViewById(R.id.order_icon);
            viewHolder.acceptTv = (TextView)convertView.findViewById(R.id.order_accept);
            viewHolder.dateTimeTv = (TextView)convertView.findViewById(R.id.order_datetime);
            viewHolder.nameTv = (TextView)convertView.findViewById(R.id.order_name);
            viewHolder.tagImage = (ImageView) convertView.findViewById(R.id.order_accept_icon);
            viewHolder.detailBtn = (TextView) convertView.findViewById(R.id.order_detail_btn);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = null;
        try {
            date = format.parse(orders.get(position).getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Order order = orders.get(position);

        viewHolder.iconImage.setImageURI(orders.get(position).getIconUrl());
        viewHolder.nameTv.setText(orders.get(position).getName());
        viewHolder.dateTimeTv.setText(f.format(date));
        String accept = orders.get(position).getIsAccept();

        if(StringUtil.isEmpty(accept)){
            viewHolder.acceptTv.setText(R.string.order_wait);
            viewHolder.tagImage.setBackgroundResource(R.drawable.order_wait);
        }else if(StringUtil.isTrue(accept)){
            viewHolder.acceptTv.setText(R.string.order_accept);
            viewHolder.tagImage.setBackgroundResource(R.drawable.order_accept);
        }else if(!StringUtil.isTrue(accept)){
            viewHolder.acceptTv.setText(R.string.order_reject);
            viewHolder.tagImage.setBackgroundResource(R.drawable.order_reject);
        }

        //点击详情
        viewHolder.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, SeeReservationInfoActivity.class);
                intent.putExtra("name",order.getSickName());
                intent.putExtra("sex",order.getSickSex());
                intent.putExtra("old",order.getSickAge());
                intent.putExtra("career",order.getSickWork());
                intent.putExtra("height",order.getSickHeight());
                intent.putExtra("weight",order.getSickWeight());
                intent.putExtra("current_symptom",order.getSickZz());
                intent.putExtra("begin_sick_time",order.getSickFbTime());
                intent.putExtra("taken_treatment",order.getSickZl());
                intent.putExtra("taken_place",order.getSickAddrJy());
                intent.putExtra("doctor_suggest",order.getDoctorSuggest());
                //Log.i("AD",order.getDoctorSuggest());

                mContext.startActivity(intent);
            }
        });

        return convertView;
    }



    class ViewHolder{
        SimpleDraweeView iconImage;
        TextView nameTv;
        TextView acceptTv;
        TextView dateTimeTv;
        ImageView tagImage;
        TextView detailBtn;
    }
}
