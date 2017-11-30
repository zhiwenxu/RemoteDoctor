package com.uestcpg.remotedoctor.Bluetooth.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 该类为多线程类，用于服务端 
 */
public class ServerThread implements Runnable {

    private Socket client = null;
    private PrintStream out;
    private BufferedReader buf;

    public ServerThread(Socket client, OutputStream other_out, InputStream this_in){
        this.client = client;
        try {
//            out = new PrintStream(client.getOutputStream());
//            buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintStream(other_out);
            buf = new BufferedReader(new InputStreamReader(this_in));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            //获取Socket的输出流，用来向客户端发送数据  
//            PrintStream out = new PrintStream(client.getOutputStream());
            //获取Socket的输入流，用来接收从客户端发送过来的数据  
//            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            boolean flag =true;
//            String str =  buf.readLine();
            String str;

            while(flag){
                //接收从客户端发送过来的数据
                str =  buf.readLine();
//                out.println(str);
                System.out.println(str);
//                if(str == null || "".equals(str)){
//                    flag = false;
//                }else{
//                    if("bye".equals(str)){
//                        flag = false;
//                    }else{
//                        将接收到的字符串前面加上echo，发送到对应的客户端
//                        out.println("echo:" + str);
//                    }
                }
            out.close();
            client.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}