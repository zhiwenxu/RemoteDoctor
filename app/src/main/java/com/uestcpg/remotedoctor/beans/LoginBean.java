package com.uestcpg.remotedoctor.beans;

/**
 * Created by dmsoft on 2017/6/19.
 */

public class LoginBean {

    private String Token;
    private String Success;
    private String Message;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
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
