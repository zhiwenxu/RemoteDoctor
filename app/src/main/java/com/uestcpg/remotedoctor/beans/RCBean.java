package com.uestcpg.remotedoctor.beans;

/**
 * Created by dmsoft on 2017/6/28.
 */

public class RCBean {
    private String Success;
    private String Message;
    private String RCToken;

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

    public String getRCToken() {
        return RCToken;
    }

    public void setRCToken(String RCToken) {
        this.RCToken = RCToken;
    }
}
