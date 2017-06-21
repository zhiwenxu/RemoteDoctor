package com.uestcpg.remotedoctor.Class;

/**
 * Created by poplx on 2017/6/21.
 */

public class Doctor {
    private String Phone;
    private String Name;
    private String Photo;
    private String Appellation;
    private String Major;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
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
