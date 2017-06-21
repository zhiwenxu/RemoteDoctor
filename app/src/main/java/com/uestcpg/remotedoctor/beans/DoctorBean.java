package com.uestcpg.remotedoctor.beans;

import com.uestcpg.remotedoctor.Class.Doctor;

import java.util.List;

/**
 * Created by poplx on 2017/6/21.
 */

public class DoctorBean {
    private String Count;
    private String Success;
    private String Message;
    private List<Doctor> doctors ;

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
