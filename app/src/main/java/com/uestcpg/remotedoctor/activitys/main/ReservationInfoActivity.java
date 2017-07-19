package com.uestcpg.remotedoctor.activitys.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.AppStatus;
import com.uestcpg.remotedoctor.app.BaseActivity;
import com.uestcpg.remotedoctor.beans.RespondBean;
import com.uestcpg.remotedoctor.network.APPUrl;
import com.uestcpg.remotedoctor.network.GsonHelper;
import com.uestcpg.remotedoctor.network.OkHttpCallBack;
import com.uestcpg.remotedoctor.network.OkHttpManager;
import com.uestcpg.remotedoctor.utils.ParamUtil;
import com.uestcpg.remotedoctor.utils.T;
import com.uestcpg.remotedoctor.views.library.AlertView;
import com.uestcpg.remotedoctor.views.library.OnConfirmeListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by poplx on 2017/7/19.
 */

public class ReservationInfoActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.reservation_time_btn)
    Button Reservation_Time_Btn;
    @InjectView(R.id.reservation_name)
    EditText Reservation_Name;
    @InjectView(R.id.reservation_sex)
    EditText Reservation_Sex;
    @InjectView(R.id.reservation_old)
    EditText Reservation_Old;
    @InjectView(R.id.reservation_career)
    EditText Reservation_Career;
    @InjectView(R.id.reservation_height)
    EditText Reservation_Height;
    @InjectView(R.id.reservation_weight)
    EditText Reservation_Weight;
    @InjectView(R.id.reservation_current_symptom)
    EditText Reservation_Current_Symptom;
    @InjectView(R.id.reservation_begin_sick_time)
    EditText Reservation_Begin_Sick_Time;
    @InjectView(R.id.reservation_taken_treatment)
    EditText Reservation_Taken_Treatment;
    @InjectView(R.id.reservation_taken_place)
    EditText Reservation_Taken_Place;

    private String doctorPhone;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);
        init();
    }
    private void init(){
        doctorPhone = getIntent().getStringExtra("doctorPhone");
        ButterKnife.inject(this);
        Reservation_Time_Btn.setOnClickListener(this);
    }


    private void Reservation(){

//        String pwd = rPasswordEdit.getText().toString();
//        String phone = rPhoneEdit.getText().toString();
//        String name = rNameEdit.getText().toString();
//
//        if(StringUtil.isEmpty(phone)){
//            T.show(this,getString(R.string.account_null_tip));
//            return;
//        }
//        if(StringUtil.isEmpty(pwd)){
//            T.show(this,getString(R.string.pwd_null_tip));
//            return;
//        }
//        if(StringUtil.isEmpty(name)){
//            T.show(this,getString(R.string.name_null_tip));
//            return;
//        }
//        String pwdMD5 = MD5Util.stringMD5(pwd);
//        ParamUtil.put("phone",phone);
//        ParamUtil.put("password",pwdMD5);
//        ParamUtil.put("name",name);
//        ParamUtil.put("doctor","false");
//        OkHttpManager.getInstance()._postAsyn(APPUrl.REGISTER_URL,ParamUtil.getParams()
//                , new OkHttpCallBack() {
//                    @Override
//                    public void onRespone(String result) {
//                        RegisterBean bean = GsonHelper.getGson().fromJson(result,RegisterBean.class);
//                        T.show(RegisterActivity.this,bean.getMessage());
//                        finish();
//                    }
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        e.printStackTrace();
//                    }
//                });

        AlertView alertView = new AlertView("预约时间",this, new OnConfirmeListener() {
            @Override
            public void result(String s) {
                ParamUtil.put("token", AppStatus.getToken());
                ParamUtil.put("sickPhone",AppStatus.getUserid());
               // ParamUtil.put("doctorPhone",doctorPhone);
                ParamUtil.put("sickName",AppStatus.getUsername());
                ParamUtil.put("dateTime",s);
                OkHttpManager.getInstance()._postAsyn(APPUrl.ORDER_YY_URL,ParamUtil.getParams(), new OkHttpCallBack() {
                    @Override
                    public void onRespone(String result) {
                        Log.e("aa",result);
                        RespondBean bean = GsonHelper.getGson().fromJson(result,RespondBean.class);
                        //T.show(DoctorInfoActivity.this,bean.getMessage());
                    }
                    @Override
                    public void onError(Request request, Exception e) {

                    }
                });
            }
        });
        alertView.show();
    }
    @Override
    public void onClick(View v) {
        if(v == Reservation_Time_Btn){
            Reservation();
        }
    }
}
