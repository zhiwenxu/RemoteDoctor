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

import com.uestcpg.remotedoctor.DoctorListAdapter;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;

/**
 * Created by dmsoft on 2017/6/14.
 */

public class DoctorListFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.friends_layout)
    RelativeLayout mFriendLayout;
    @InjectView(R.id.friends_icon)
    ImageView mFriendIcon;
    @InjectView(R.id.friends_name)
    TextView mFriendName;
    @InjectView(R.id.friends_list)
    ListView mFriendsList;

    private List<Map<String, Object>> data;

    public static DoctorListFragment getInstance(){
        return new DoctorListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_friends,null);
        ButterKnife.inject(this,contentView);

        data = getData();
        DoctorListAdapter adapter = new DoctorListAdapter(this);
        mFriendsList.setAdapter(adapter);

        init();


        return contentView;
    }

    private List<Map<String, Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(int i=0;i<10;i++)
        {
            map = new HashMap<String, Object>();
            map.put("doctor_photo", R.drawable.doctor);
            map.put("doctor_name", "李时珍");
            map.put("doctor_appellation", "主治医师");
            map.put("doctor_major", "儿科");
            map.put("advisory", "咨询");
            list.add(map);
        }
        return list;
    }

    private void init(){
        mFriendName.setText(AppStatus.getTagetId());
        mFriendLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mFriendLayout){
            RongIM.getInstance().startPrivateChat(getActivity(),AppStatus.getTagetId(),AppStatus.getUsername());
        }
    }
}
