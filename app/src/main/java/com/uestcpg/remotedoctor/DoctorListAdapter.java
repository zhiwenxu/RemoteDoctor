package com.uestcpg.remotedoctor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.uestcpg.remotedoctor.Class.Doctor;


/**
 * Created by poplx on 2017/6/21.
 */

public class DoctorListAdapter extends BaseAdapter{
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Doctor holder = null;
        //如果缓存convertView为空，则需要创建View
        if(convertView == null)
        {
            holder = new Doctor();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.title = (TextView)convertView.findViewById(R.id.tv);
            holder.info = (TextView)convertView.findViewById(R.id.info);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.img.setImageResource((Integer) data.get(position).get("img"));
        holder.title.setText((String)data.get(position).get("title"));
        holder.info.setText((String)data.get(position).get("info"));

        return convertView;
    }
}
