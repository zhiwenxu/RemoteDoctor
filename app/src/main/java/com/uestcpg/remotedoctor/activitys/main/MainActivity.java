package com.uestcpg.remotedoctor.activitys.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.BaseFragmentActivity;
import com.uestcpg.remotedoctor.fragments.tab.DoctorListFragment;
import com.uestcpg.remotedoctor.fragments.tab.MeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by dmsoft on 2017/6/14.
 */

public class MainActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    @InjectView(R.id.main_viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.tab_message_im)
    ImageView mMsgIm;
    @InjectView(R.id.tab_friends_im)
    ImageView mFriendIm;
    @InjectView(R.id.tab_me_im)
    ImageView mMeIm;
    @InjectView(R.id.tab_message_tv)
    TextView mMsgTv;
    @InjectView(R.id.tab_friends_tv)
    TextView mFriendsTv;
    @InjectView(R.id.tab_me_tv)
    TextView mMeTv;
    @InjectView(R.id.tab_message)
    LinearLayout mMsgLayout;
    @InjectView(R.id.tab_friends)
    LinearLayout mFriendLayout;
    @InjectView(R.id.tab_me)
    LinearLayout mMeLayout;

    private FragmentManager fm;
    private FragmentTransaction ft;
    private DoctorListFragment friendsListFragment;
    private MeFragment meFragment;
    private ConversationListFragment mConversationListFragment;
    private List<Fragment> mFragments = new ArrayList<>();
    private Conversation.ConversationType[] mConversationsTypes = null;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews(){
        initTitle();
        setCenterTv("消息");
        changeSelectedTabState(0);
        mMsgLayout.setOnClickListener(this);
        mFriendLayout.setOnClickListener(this);
        mMeLayout.setOnClickListener(this);

        fm = getSupportFragmentManager();
        mConversationListFragment = (ConversationListFragment) initConversationList();
        friendsListFragment = DoctorListFragment.getInstance();
        meFragment = MeFragment.getInstance();
        mFragments.add(mConversationListFragment);
        mFragments.add(friendsListFragment);
        mFragments.add(meFragment);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(this);
    }
    //初始化tab
    private void changeTextViewColor() {
        mMsgIm.setBackgroundResource(R.drawable.home_bottom_tab_notification_press_up);
        mFriendIm.setBackgroundResource(R.drawable.home_bottom_tab_chat_press_up);
        mMeIm.setBackgroundResource(R.drawable.home_bottom_tab_settings_press_up);
        mMsgTv.setTextColor(getResources().getColor(R.color.text_color_gray));
        mFriendsTv.setTextColor(getResources().getColor(R.color.text_color_gray));
        mMeTv.setTextColor(getResources().getColor(R.color.text_color_gray));
    }

    private void changeSelectedTabState(int position) {
        changeTextViewColor();
        switch (position) {
            case 0:
                mMsgIm.setBackgroundResource(R.drawable.home_bottom_tab_notification_press_down);
                mMsgTv.setTextColor(getResources().getColor(R.color.app_common_color_green));
                setCenterTv("消息");
                break;
            case 1:
                mFriendIm.setBackgroundResource(R.drawable.home_bottom_tab_chat_press_down);
                mFriendsTv.setTextColor(getResources().getColor(R.color.app_common_color_green));
                setCenterTv("医生");
                break;
            case 2:
                mMeIm.setBackgroundResource(R.drawable.home_bottom_tab_settings_press_down);
                mMeTv.setTextColor(getResources().getColor(R.color.app_common_color_green));
                setCenterTv("我");
                break;

        }
    }


    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.DISCUSSION
            };
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if(v == mMsgLayout){
            mViewPager.setCurrentItem(0,false);
        }
        if(v == mFriendLayout){
            mViewPager.setCurrentItem(1,false);
        }
        if(v == mMeLayout){
            mViewPager.setCurrentItem(2,false);
        }
    }
}
