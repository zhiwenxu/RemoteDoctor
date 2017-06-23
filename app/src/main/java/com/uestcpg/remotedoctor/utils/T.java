package com.uestcpg.remotedoctor.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dmsoft on 2017/6/21.
 */

public class T {
    public static void show(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
