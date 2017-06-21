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

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;

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

    public static DoctorListFragment getInstance(){
        return new DoctorListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_friends,null);
        ButterKnife.inject(this,contentView);

        init();
        return contentView;
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
