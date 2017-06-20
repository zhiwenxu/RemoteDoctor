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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;

/**
 * Created by dmsoft on 2017/6/14.
 */

public class FriendsListFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.friends_layout)
    RelativeLayout mFriendLayout;
    @InjectView(R.id.friends_icon)
    ImageView mFriendIcon;
    @InjectView(R.id.friends_name)
    TextView mFriendName;

    public static FriendsListFragment getInstance(){
        return new FriendsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_friends,null);
        ButterKnife.inject(this,contentView);

        //new
        //绑定XML中的ListView，作为Item的容器
        ListView list = (ListView) contentView.findViewById(R.id.MyListView);

        //生成动态数组，并且转载数据
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<30;i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "This is Title.....");
            map.put("ItemText", "This is text.....");
            mylist.add(map);
        }
        //生成适配器，数组===》ListItem
        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), //没什么解释
                mylist,//数据来源
                R.layout.list_item,//ListItem的XML实现

                //动态数组与ListItem对应的子项
                new String[] {"ItemTitle", "ItemText"},


                //ListItem的XML文件里面的两个TextView ID
                new int[] {R.id.ItemTitle,R.id.ItemText});
        //添加并且显示
        list.setAdapter(mSchedule);

        //end new


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
