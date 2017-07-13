package com.uestcpg.remotedoctor.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.main.PhotoViewActivity;

/**
 * Created by dmsoft on 2017/7/13.
 */

public class SickRecordAdapter extends RecyclerView.Adapter<SickRecordAdapter.ViewHolder>{

    private Context mContext;

    public SickRecordAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from( mContext).inflate(R.layout.sick_record_list_item,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(R.drawable.sick_record1);
            }
        });
        holder.p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(R.drawable.sick_record2);
            }
        });
        holder.p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(R.drawable.sick_record3);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView p1;
        ImageView p2;
        ImageView p3;
        public ViewHolder(View view) {
            super(view);
            p1 = (ImageView)view.findViewById(R.id.p1);
            p2 = (ImageView)view.findViewById(R.id.p2);
            p3 = (ImageView)view.findViewById(R.id.p3);
        }
    }

    private void start(int res){
        Intent intent = new Intent(mContext, PhotoViewActivity.class);
        intent.putExtra("pic",res);
        mContext.startActivity(intent);
    }
}
