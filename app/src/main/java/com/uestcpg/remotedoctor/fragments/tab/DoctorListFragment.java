package com.uestcpg.remotedoctor.fragments.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.Class.Doctor;
import com.uestcpg.remotedoctor.DoctorListAdapter;
import com.uestcpg.remotedoctor.R;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;

/**
 * Created by dmsoft on 2017/6/14.
 *
 */

public class DoctorListFragment extends Fragment implements View.OnClickListener{

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
        mDoctorListAdapter = new DoctorListAdapter(getActivity(),doctors);
        mFriendsList.setAdapter(mDoctorListAdapter);
    }

    private void getData(){
        OkHttpManager.getInstance()._getAsyn(APPUrl.GET_DOCTOR_URL, AppStatus.getToken(), new OkHttpCallBack() {
            @Override
            public void onRespone(String result) {
                DoctorBean bean = GsonHelper.getGson().fromJson(result,DoctorBean.class);
                if(StringUtil.isTrue(bean.getSuccess())){
                    mDoctorListAdapter.addDatas(bean.getDoctors());
                }
                T.show(getActivity(),bean.getMessage());
            }
            @Override
            public void onError(Request request, Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
//        if(v == mFriendLayout){
//            RongIM.getInstance().startPrivateChat(getActivity(),AppStatus.getTagetId(),AppStatus.getUsername());
//        }
    }
}
