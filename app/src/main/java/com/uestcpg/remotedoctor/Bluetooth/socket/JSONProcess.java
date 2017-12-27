package com.uestcpg.remotedoctor.Bluetooth.socket;

import com.google.gson.Gson;

/**
 * Created by John on 2017/8/1.
 */

public class JSONProcess {
    private Gson gson = new Gson();

    public class Frame{
        public String s;
        public String r;
        public byte[] c;
    }

    public String toJson(String patient_phone_num, String doctor_phone_num, byte[] content){

        Frame frame = new Frame();
        frame.s = patient_phone_num;
        frame.r = doctor_phone_num;
        frame.c = content;
        return gson.toJson(frame);
    }

    public Frame fromJson(String frame){
        return gson.fromJson(frame, Frame.class);
    }

}
