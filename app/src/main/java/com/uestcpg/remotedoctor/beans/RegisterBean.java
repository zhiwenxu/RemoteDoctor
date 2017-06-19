package com.uestcpg.remotedoctor.beans;

/**
 * Created by dmsoft on 2017/6/19.
 */

public class RegisterBean {

    private boolean Success;
    private String Message;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
