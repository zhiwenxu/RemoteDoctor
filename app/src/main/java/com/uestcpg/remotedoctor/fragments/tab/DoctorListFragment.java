package com.uestcpg.remotedoctor.fragments.tab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.Class.Doctor;
import com.uestcpg.remotedoctor.adapters.DoctorListAdapter;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.activitys.main.DoctorInfoActivity;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.beans.DoctorBean;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by dmsoft on 2017/6/14.
 *
 */

public class DoctorListFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener,RongIM.UserInfoProvider{

    @InjectView(R.id.friends_list)
    ListView mFriendsList;

    private DoctorListAdapter mDoctorListAdapter;
    private List<Doctor> doctors = new ArrayList<>();

    public static DoctorListFragment getInstance(){
        return new DoctorListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_friends,null);
        ButterKnife.inject(this,contentView);
        init();
        getData();
        return contentView;
    }

    private void init(){
        RongIM.setUserInfoProvider(this,true);
        mDoctorListAdapter = new DoctorListAdapter(getActivity(),doctors);
        mFriendsList.setAdapter(mDoctorListAdapter);
        mFriendsList.setOnItemClickListener(this);
    }

    private void getData(){
        ParamUtil.put("token",AppStatus.getToken());
        OkHttpManager.getInstance()._postAsyn(APPUrl.GET_DOCTOR_URL,ParamUtil.getParams(),new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                DoctorBean bean = GsonHelper.getGson().fromJson(result,DoctorBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    mDoctorListAdapter.addDatas(bean.getDoctors());
                }else{
                    T.show(getActivity(),bean.getMessage());
                }
            }
            @Override
            public void onError(Request request, Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
//        if(v == mFriendLayout){
//
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), DoctorInfoActivity.class);
        intent.putExtra("doctorPhone",doctors.get(position).getPhone());
        startActivity(intent);
    }

    @Override
    public UserInfo getUserInfo(String s) {

        Log.e("s",s);
        for(Doctor doctor : doctors){
            Log.e("do",doctor.getPhone()+"|"+doctor.getName()+"|"+doctor.getPhoto());

            return new UserInfo(doctor.getPhone(),doctor.getName(), Uri.parse(doctor.getPhoto()));
        }
        if(AppStatus.getUsername() != null){
            return new UserInfo(AppStatus.getUserid(),AppStatus.getUsername(),Uri.parse(AppStatus.getUrl()));
        }
        return null;
    }
}
