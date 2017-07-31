package com.uestcpg.remotedoctor.Bluetooth;

import java.io.ByteArrayOutputStream;

/**
 * Created by dell on 2017/6/23.
 */
public class StringHexUtils{
        // 转化字符串为十六进制编码
        public static String toHexString(String s) {
            String str = "";
            for (int i = 0; i < s.length(); i++) {
                int ch = (int) s.charAt(i);
                String s4 = Integer.toHexString(ch);
                str = str + s4;
            }
            return str;
        }

        /**
         * 转化十六进制编码为字符串
         *
         * @param s
         * @return
         */
        public static String toStringHex(String s) {
            byte[] baKeyword = new byte[s.length() / 2];
            for (int i = 0; i < baKeyword.length; i++) {
                try {
                    baKeyword[i] = (byte) (0xff & Integer.parseInt(
                            s.substring(i * 2, i * 2 + 2), 16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                s = new String(baKeyword, "utf-8");// UTF-16le:Not
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return s;
        }

        /**
         * 16进制数字字符集
         */
        private static String hexString = "0123456789ABCDEF";

        /**
         * 将字符串编码成16进制数字,适用于所有字符（包括中文）
         */
        public static String encode(String str) {
            // 根据默认编码获取字节数组
            byte[] bytes = str.getBytes();
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            // 将字节数组中每个字节拆解成2位16进制整数
            for (int i = 0; i < bytes.length; i++) {
                sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
                sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
                sb.append(' ');
            }
            return sb.toString();
        }

        /**
         * 将16进制数字解码成字符串,适用于所有字符（包括中文）
         */
        public static String decode(String bytes) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(
                    bytes.length() / 2);
            // 将每2位16进制整数组装成一个字节
            for (int i = 0; i < bytes.length(); i += 2)
                baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                        .indexOf(bytes.charAt(i + 1))));
            return new String(baos.toByteArray());
        }

        /**
         * 将字节数组转换为十六进制字符串（包括中文）
         */
        public static String Bytes2HexString(byte[] b) {
            String ret = "";
            for (int i = 0; i < b.length; i++) {
                String hex = Integer.toHexString(b[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                ret += hex.toUpperCase();
            }
            return ret;
        }

        public static byte[] hexStr2Bytes(String paramString)
        {
            int i = paramString.length() / 2;
            System.out.println(i);
            byte[] arrayOfByte = new byte[i];
            int j = 0;
            while (true)
            {
                if (j >= i)
                    return arrayOfByte;
                int k = 1 + j * 2;
                int l = k + 1;
                arrayOfByte[j] = (byte)(0xFF & Integer.decode("0x" + paramString.substring(j * 2, k) + paramString.substring(k, l)).intValue());
                ++j;
            }
        }

        public static StringBuilder getHexByte(byte[] b,int start, int length){
//            int t = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = start; i < start + length; i++) {

//                if(i > 2 && b[i-1] == 'U' && b[i-2] == 'U'){
//                    break;
//                }
                String hex = Integer.toHexString(b[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(" ").append(hex.toUpperCase());

//                t++;
//                if(t % 16 == 0){
//                    sb.append("\n");
//                }
            }
//            sb.append("\n");
            return sb;
        }

        public static String hexStr2Str(String paramString)
        {
            char[] arrayOfChar = paramString.toCharArray();
            byte[] arrayOfByte = new byte[paramString.length() / 2];
            int i = 0;
            while (true)
            {
                if (i >= arrayOfByte.length)
                    return new String(arrayOfByte);
                arrayOfByte[i] = (byte)(0xFF & 16 * "0123456789ABCDEF".indexOf(arrayOfChar[(i * 2)]) + "0123456789ABCDEF".indexOf(arrayOfChar[(1 + i * 2)]));
                ++i;
            }
        }

    public static byte generateByte(String s) {

        byte tmp = 0;
        char[] arr = s.toCharArray();
        if(arr.length > 2){
            return tmp;
        }
        int t0 = Integer.parseInt(Character.toString(arr[0]), 16);
        int t1 = Integer.parseInt(Character.toString(arr[1]), 16);
        byte tmp0 = (byte)t0;
        byte tmp1 = (byte)t1;
        tmp = (byte) (tmp0 << 4);
        tmp = (byte) (tmp | tmp1);
        return tmp;
    }
}
