package com.uestcpg.remotedoctor.utils;

import android.annotation.TargetApi;
import android.icu.util.Calendar;
import android.os.Build;

/**
 * Created by dmsoft on 2017/6/30.
 */

public class CalenderUtil {

    @TargetApi(Build.VERSION_CODES.N)
    public static int getYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
}
