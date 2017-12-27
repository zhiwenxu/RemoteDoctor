package com.uestcpg.remotedoctor.Bluetooth.socket;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by John on 2017/8/1.
 */


public class MultiClient {
//    static final int MAX_THREADS = 40;

    public void getConnected() throws IOException{
        byte[] bs = new byte[] { (byte) 127, (byte) 0, 0, (byte)1};
        InetAddress addr = InetAddress.getByAddress(bs);
//        new SocketClientThread();
    }

    public static void main(String[] args) throws IOException,
            InterruptedException {

        byte a = (byte)0x55;
        byte b = (byte)0xAA;
        byte c = (byte)0xB6;
        byte d = (byte)0x97;
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
//        byte[] bs = new byte[] { (byte) 127, (byte) 0, 0, (byte)1};
//        InetAddress addr = InetAddress.getByAddress(bs);
//        InetAddress addr = InetAddress.getByName("www.xiaopeng.site/6666");
//        InetAddress addr = InetAddress.getByName("www.xiaopeng.site/6666");
//        InetAddress addr = InetAddress.getByName("www.xiaopeng.site:3389");
//        InetAddress addr = InetAddress.getByName("www.xiaopeng.site:6688");
//        System.out.println(addr);

//        new SocketClientThread();

//        while (true) {
//            if (SocketClientThread.threadCount() < MAX_THREADS)
//                new SocketClientThread(addr);
//            Thread.currentThread().sleep(100);

//        byte[] array = new byte[]{1, 2, 3, 4, 5, 6,7,8,9,10};
//        JSONProcess json_process = new JSONProcess();
//        String frame = json_process.toJson("15108307486", "15108307485", array) + "\n";
//        System.out.println(frame);

//        JSONProcess.Frame frame1 = json_process.fromJson(frame);
//        byte[] n = frame1.c;
//        System.out.println(n[0]);
    }
}
