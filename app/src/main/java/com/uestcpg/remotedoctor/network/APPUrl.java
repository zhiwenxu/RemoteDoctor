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
    public static final String SICK_INFO_URL = SERVER_URL+"sickInfo";//获得病人信息
    public static final String SET_SICK_INFO_URL = SERVER_URL+"setSickInfo";//设置病人信息
    public static final String GET_DOCTOR_INFO_URL = SERVER_URL+"getDoctorInfo";//获取医生信息
    public static final String ORDER_YY_URL = SERVER_URL+"orderYy";//发起预约
    public static final String GET_OREDER_URL = SERVER_URL+"sickOrder";//获取预约信息
    public static final String CHECK_ORDER_URL = SERVER_URL+"sickCheckOrder";//获取是否被接受预约信息

}
