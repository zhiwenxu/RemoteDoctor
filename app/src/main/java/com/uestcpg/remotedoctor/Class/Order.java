package com.uestcpg.remotedoctor.Class;

/**
 * Created by dmsoft on 2017/7/5.
 */

public class Order {

    private String name;
    private String iconUrl;
    private String dateTime;
    private String IsAccept;
    private String doctorPhone;
    private String reason;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getIsAccept() {
        return IsAccept;
    }

    public void setIsAccept(String isAccept) {
        IsAccept = isAccept;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
