package com.uestcpg.remotedoctor.Bluetooth.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by John on 2017/8/2.
 */

public class ServerMain {

    public static void main(String[] args) throws Exception{
        //服务端在20006端口监听客户端请求的TCP连接
        ServerSocket server = new ServerSocket(6688);
        Socket client1 = null;
        Socket client2 = null;

        boolean f = true;
//        while(f){
            //等待客户端的连接，如果没有获取连接
//        while(f) {
            client1 = server.accept();
            System.out.println("与客户端连接成功！");
            //为每个客户端连接开启一个线程
            InputStream in_stream1 = client1.getInputStream();
            OutputStream out_stream1 = client1.getOutputStream();

//        client2 = server.accept();
//        InputStream in_stream2 = client2.getInputStream();
//        OutputStream out_stream2 = client2.getOutputStream();

//        new Thread(new ServerThread(client1, out_stream1, in_stream2)).start();
            new Thread(new ServerThread(client1, out_stream1, in_stream1)).start();
//        }
//        new Thread(new ServerThread(client2, out_stream2, in_stream1)).start();

//        }

//        out = new PrintStream(client.getOutputStream());
//        buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        server.close();
    }
}
