package com.uestcpg.remotedoctor.fragments.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uestcpg.remotedoctor.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dmsoft on 2017/6/14.
 */

public class MeFragment extends Fragment {

    public static MeFragment getInstance(){
        return new MeFragment();
    }

    @InjectView(R.id.change_information_btn)
    Button Change_Information_btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_me,null);
        ButterKnife.inject(this,contentView);
        init();
        return contentView;
    }
    private void init(){

    }

    private void ChangeInfomation(){
        Intent intent = new Intent(MeFragment.this, MeFragmentChange.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == Change_Information_btn){
            ChangeInfomation();
        }
    }

}
