package com.uestcpg.remotedoctor.utils;

import java.util.Date;

/**
 * Created by dmsoft on 2017/6/30.
 *
 */

public class CalenderUtil {

    public static int getYear(){
        Date date = new Date(System.currentTimeMillis());
        return date.getYear();
    }

}
