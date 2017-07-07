package com.uestcpg.remotedoctor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uestcpg.remotedoctor.Class.Doctor;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;
import java.util.List;

import io.rong.imkit.RongIM;


/**
 * Created by poplx on 2017/6/21.
 *
 */

public class DoctorListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Doctor> doctors;

    public DoctorListAdapter(Context context,List<Doctor> datas) {
        this.mContext = context;
        this.doctors = datas;
    }
    //添加所有数据
    public void addDatas(List<Doctor> datas){
        doctors.addAll(datas);
        notifyDataSetChanged();
    }
    //添加单一数据
    public void addData(Doctor doctor){
        doctors.add(doctor);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return doctors.size();
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
        ViewHolder holder = null;
        final Doctor doctor = doctors.get(i);
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.doctor_list_item,null);
            holder = new ViewHolder();
            holder.iconImage = (SimpleDraweeView)convertView.findViewById(R.id.doctor_icon);
            holder.nameTv = (TextView)convertView.findViewById(R.id.doctor_name);
            holder.appellationTv = (TextView)convertView.findViewById(R.id.doctor_appellation);
            holder.majorTv = (TextView)convertView.findViewById(R.id.doctor_major);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iconImage.setImageURI(doctor.getPhoto());
        holder.nameTv.setText(doctor.getName());
        holder.appellationTv.setText(doctor.getAppellation());
        holder.majorTv.setText(mContext.getString(R.string.be_kind_at)+doctor.getMajor());
        return convertView;
    }
    private class ViewHolder{
        SimpleDraweeView iconImage;
        TextView nameTv;
        TextView appellationTv;
        TextView majorTv;
    }
}
