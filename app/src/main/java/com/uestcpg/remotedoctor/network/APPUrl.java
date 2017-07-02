package com.uestcpg.remotedoctor.network;

/**
 * Created by xzw on 2017/6/19.
 * 保存应用的所有访问服务器地址
 */

public class APPUrl {
    private static final String SERVER_URL = "http://1750q5u152.iask.in:30038/RemoteDoctorServer/";
    public static final String REGISTER_URL = SERVER_URL+"register";  //注册地址
    public static final String LOGIN_URL = SERVER_URL+"login"; //登录地址
    public static final String GET_DOCTOR_URL = SERVER_URL+"getDoctor";//获得医生地址
}
