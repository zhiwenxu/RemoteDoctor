package com.uestcpg.remotedoctor.activitys.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uestcpg.remotedoctor.R;
import com.uestcpg.remotedoctor.app.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by poplx on 2017/7/20.
 */

public class SeeReservationInfoActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.see_reservation_confirm_btn)
    Button Reservation_confirm_Btn;
    @InjectView(R.id.see_reservation_name)
    TextView Reservation_Name;
    @InjectView(R.id.see_reservation_sex)
    TextView Reservation_Sex;
    @InjectView(R.id.see_reservation_old)
    TextView Reservation_Old;
    @InjectView(R.id.see_reservation_career)
    TextView Reservation_Career;
    @InjectView(R.id.see_reservation_height)
    TextView Reservation_Height;
    @InjectView(R.id.see_reservation_weight)
    TextView Reservation_Weight;
    @InjectView(R.id.see_reservation_current_symptom)
    TextView Reservation_Current_Symptom;
    @InjectView(R.id.see_reservation_begin_sick_time)
    TextView Reservation_Begin_Sick_Time;
    @InjectView(R.id.see_reservation_taken_treatment)
    TextView Reservation_Taken_Treatment;
    @InjectView(R.id.see_reservation_taken_place)
    TextView Reservation_Taken_Place;
    @InjectView(R.id.see_reservation_doctor_suggest)
    TextView Reservation_Doctor_Suggest;

    private String Rname;
    private String Rsex;
    private String Rold;
    private String Rcareer;
    private String Rheight;
    private String Rweight;
    private String Rcurrent_symptom;
    private String Rbegin_sick_time;
    private String Rtaken_treatment;
    private String Rtaken_place;
    private String Rdoctor_suggest;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_reservation);
        ButterKnife.inject(this);
        init();
    }
    private void init(){
        Rname = getIntent().getStringExtra("name");
        Rsex = getIntent().getStringExtra("sex");
        Rold = getIntent().getStringExtra("old");
        Rcareer = getIntent().getStringExtra("career");
        Rheight = getIntent().getStringExtra("height");
        Rweight = getIntent().getStringExtra("weight");
        Rcurrent_symptom = getIntent().getStringExtra("current_symptom");
        Rbegin_sick_time = getIntent().getStringExtra("begin_sick_time");
        Rtaken_treatment = getIntent().getStringExtra("taken_treatment");
        Rtaken_place = getIntent().getStringExtra("taken_place");
        Rdoctor_suggest = getIntent().getStringExtra("doctor_suggest");
        Reservation_confirm_Btn.setOnClickListener(this);
        Reservation();
        //Log.i("A123",Rdoctor_suggest);
    }

    private void getInfo(){

        Reservation_Name.setText(Rname);
        Reservation_Sex.setText(Rsex);
        Reservation_Old.setText(Rold);
        Reservation_Career.setText(Rcareer);
        Reservation_Height.setText(Rheight);
        Reservation_Weight.setText(Rweight);
        Reservation_Current_Symptom.setText(Rcurrent_symptom);
        Reservation_Begin_Sick_Time.setText(Rbegin_sick_time);
        Reservation_Taken_Treatment.setText(Rtaken_treatment);
        Reservation_Taken_Place.setText(Rtaken_place);
        Reservation_Doctor_Suggest.setText(Rdoctor_suggest);
    }


    private void Reservation(){

        Reservation_Name.setText(Rname);
        Reservation_Sex.setText(Rsex);
        Reservation_Old.setText(Rold);
        Reservation_Career.setText(Rcareer);
        Reservation_Height.setText(Rheight);
        Reservation_Weight.setText(Rweight);
        Reservation_Current_Symptom.setText(Rcurrent_symptom);
        Reservation_Begin_Sick_Time.setText(Rbegin_sick_time);
        Reservation_Taken_Treatment.setText(Rtaken_treatment);
        Reservation_Taken_Place.setText(Rtaken_place);
        Reservation_Doctor_Suggest.setText(Rdoctor_suggest);
    }
    @Override
    public void onClick(View v) {
        if(v == Reservation_confirm_Btn){
            finish();
        }
    }
}
