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
import com.uestcpg.remotedoctor.utils.SPUtil;
import com.uestcpg.remotedoctor.utils.StringUtil;
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
        ButterKnife.inject(this);
        doctorPhone = getIntent().getStringExtra("doctorPhone");
        Reservation_Name.setText(SPUtil.getReservation_Name(this));
        Reservation_Sex.setText(SPUtil.getReservation_Sex(this));
        Reservation_Old.setText(SPUtil.getReservation_Old(this));
        Reservation_Career.setText(SPUtil.getReservation_Career(this));
        Reservation_Height.setText(SPUtil.getReservation_Height(this));
        Reservation_Weight.setText(SPUtil.getReservation_Weight(this));
        Reservation_Time_Btn.setOnClickListener(this);
    }


    private void Reservation(){

        final String name = Reservation_Name.getText().toString();
        final String sex = Reservation_Sex.getText().toString();
        final String old = Reservation_Old.getText().toString();
        final String career = Reservation_Career.getText().toString();
        final String height = Reservation_Height.getText().toString();
        final String weight = Reservation_Weight.getText().toString();
        final String current_symptom = Reservation_Current_Symptom.getText().toString();
        final String begin_sick_time = Reservation_Begin_Sick_Time.getText().toString();
        final String taken_treatment = Reservation_Taken_Treatment.getText().toString();
        final String taken_place = Reservation_Taken_Place.getText().toString();


        if(StringUtil.isEmpty(name)){
            T.show(this,getString(R.string.account_null_tip));
            return;
        }
        if(StringUtil.isEmpty(sex)){
            T.show(this,getString(R.string.sex_null_tip));
            return;
        }
        if(StringUtil.isEmpty(old)){
            T.show(this,getString(R.string.old_null_tip));
            return;
        }
        if(StringUtil.isEmpty(career)){
            T.show(this,getString(R.string.career_null_tip));
            return;
        }
        if(StringUtil.isEmpty(height)){
            T.show(this,getString(R.string.height_null_tip));
            return;
        }
        if(StringUtil.isEmpty(weight)){
            T.show(this,getString(R.string.weight_null_tip));
            return;
        }
        if(StringUtil.isEmpty(current_symptom)){
            T.show(this,getString(R.string.current_symptom_null_tip));
            return;
        }
        if(StringUtil.isEmpty(begin_sick_time)){
            T.show(this,getString(R.string.begin_sick_time_null_tip));
            return;
        }
        if(StringUtil.isEmpty(taken_treatment)){
            T.show(this,getString(R.string.taken_treatment_null_tip));
            return;
        }
        if(StringUtil.isEmpty(taken_place)){
            T.show(this,getString(R.string.taken_place_null_tip));
            return;
        }


        AlertView alertView = new AlertView("预约时间",this, new OnConfirmeListener() {
            @Override
            public void result(String s) {
                ParamUtil.put("token", AppStatus.getToken());
                ParamUtil.put("sickPhone",AppStatus.getUserid());
                ParamUtil.put("doctorPhone",doctorPhone);
                ParamUtil.put("sickName",name);
                ParamUtil.put("dateTime",s);
                ParamUtil.put("sickAge",old);
                ParamUtil.put("sickSex",sex);
                ParamUtil.put("sickWork",career);
                ParamUtil.put("sickHeight",height);
                ParamUtil.put("sickWeight",weight);
                ParamUtil.put("sickZz",current_symptom);
                ParamUtil.put("sickFbTime",begin_sick_time);
                ParamUtil.put("sickZl",taken_treatment);
                ParamUtil.put("sickAddrJy",taken_place);


                OkHttpManager.getInstance()._postAsyn(APPUrl.ORDER_YY_URL,ParamUtil.getParams(), new OkHttpCallBack() {
                    @Override
                    public void onRespone(String result) {
                        Log.e("aa",result);
                        RespondBean bean = GsonHelper.getGson().fromJson(result,RespondBean.class);
                        T.show(ReservationInfoActivity.this,bean.getMessage());
                        SPUtil.setReservation_Name(ReservationInfoActivity.this,Reservation_Name.getText().toString().trim());
                        SPUtil.setReservation_Sex(ReservationInfoActivity.this,Reservation_Sex.getText().toString().trim());
                        SPUtil.setReservation_Old(ReservationInfoActivity.this,Reservation_Old.getText().toString().trim());
                        SPUtil.setReservation_Career(ReservationInfoActivity.this,Reservation_Career.getText().toString().trim());
                        SPUtil.setReservation_Height(ReservationInfoActivity.this,Reservation_Height.getText().toString().trim());
                        SPUtil.setReservation_Weight(ReservationInfoActivity.this,Reservation_Weight.getText().toString().trim());
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
