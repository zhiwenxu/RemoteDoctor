package com.uestcpg.remotedoctor.Bluetooth;

import android.util.Log;

public class DataConvert
{
    // 十六进制字符串转换为字节
    public static byte hexStr2Bytes(String paramString)
    {
        int j = paramString.length() / 2;
        System.out.println(j);
        byte[] arrayOfByte = new byte[j];
        int i = 0;
        for (;;)
        {
            if (i >= j) {
                return arrayOfByte[0];
            }
            int k = i * 2 + 1;
            arrayOfByte[i] = Byte.decode("0x" + paramString.substring(i * 2, k) + paramString.substring(k, k + 1)).byteValue();
            i += 1;
        }
    }

    // 字节转换为十六进制字符串
    public static String Chr2Hex(CharSequence paramCharSequence)
    {
        String str = "";
        int i = 0;
        for (;;)
        {
            if (i >= paramCharSequence.length()) {
                return str;
            }
            str = str + Integer.toHexString(paramCharSequence.charAt(i) | 0x100).substring(1) + " ";
            i += 1;
        }
    }

    // 十六进制字符串转换为字节
    public static String Hex2Chr(String paramString)
    {
        String str = paramString.replace(" ", "");
        Log.d("----", str);
        char[] arrayOfChar = str.toCharArray();
        paramString = "";
        int i = 0;
        for (;;)
        {
            if (i >= str.length() / 2 - 1) {
                return paramString;
            }
            int j = Character.digit(arrayOfChar[(i * 2)], 16);
            int k = Character.digit(arrayOfChar[(i * 2 + 1)], 16);
            paramString = paramString + (char)((j << 4) + k);
            i += 1;
        }
    }

    // 十六进制字符串转换为字节
    public static byte[] Hex2byte(String paramString)
    {
        paramString = paramString.replace(" ", "");
        byte[] arrayOfByte = new byte[paramString.length() / 2];
        char[] arrayOfChar = paramString.toCharArray();
        int i = 0;
        for (;;)
        {
            if (i >= paramString.length() / 2) {
                return arrayOfByte;
            }
            arrayOfByte[i] = ((byte)((Character.digit(arrayOfChar[(i * 2)], 16) << 4) + Character.digit(arrayOfChar[(i * 2 + 1)], 16) & 0xFF));
            i += 1;
        }
    }
}

