package com.uestcpg.remotedoctor.beans;

/**
 * Created by dmsoft on 2017/7/5.
 */

public class DoctorInfoBean {

    private String Success;
    private String Message;
    private String Name;
    private String IconUrl;
    private String Appellation;
    private String Major;

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

    public String getAppellation() {
        return Appellation;
    }

    public void setAppellation(String appellation) {
        Appellation = appellation;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }
}
