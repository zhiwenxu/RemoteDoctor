package com.uestcpg.remotedoctor.beans;

/**
 * Created by dmsoft on 2017/7/5.
 */

public class SickInfoBean {

    private String Success;
    private String Message;
    private String Name;
    private String IconUrl;
    private String Desception;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIconUrl() {
        return IconUrl;
    }

    public void setIconUrl(String iconUrl) {
        IconUrl = iconUrl;
    }

    public String getDesception() {
        return Desception;
    }

    public void setDesception(String desception) {
        Desception = desception;
    }
}
