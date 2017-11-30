package com.uestcpg.remotedoctor.Bluetooth.socket;

import com.uestcpg.remotedoctor.Bluetooth.Auth;
import com.uestcpg.remotedoctor.Bluetooth.ShareBuffer;
import com.uestcpg.remotedoctor.Bluetooth.common.logger.Log;
import com.uestcpg.remotedoctor.app.AppStatus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by John on 2017/8/1.
 */

public class SocketClientThread extends Thread{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static int counter = 0;
    private int id = counter++;
    private static int threadcount = 0;
    private int PORT = 6688;
    private JSONProcess process = new JSONProcess();
    private OutputStream btOutputStream;
    private InputStream btInputStream;
    private String s;
    private String r;
    private BlockingQueue basket;
    public BufferedOutputStream writer;
    public BufferedInputStream reader;
    private ShareBuffer shareBuffer;
    private boolean suspendFlag;

//    public static int threadCount() {
//        return threadcount;
//    }

    public PrintWriter getSocketWriter(){
        return out;
    }

    public String toJson(byte[] frame, int length){
        byte[] tmp = new byte[length];
        System.arraycopy(frame, 0, tmp, 0, length);
        return process.toJson(s, r, tmp);
    }

    //OutputStream btOutputStream_t,
    //InputStream btInputStream_t
    //BufferedInputStream reader
    public SocketClientThread(BlockingQueue basket, ShareBuffer shareBuffer) throws IOException{
        System.out.println("Making client " + id);
        threadcount++;

        byte[] bs = new byte[] { (byte)192, (byte)168, 1, (byte)112};
//        InetAddress addr = InetAddress.getByAddress(bs);

//            start();

        this.basket = basket;
        this.shareBuffer = Auth.shareBuffer1;
//        btOutputStream = btOutputStream_t;
//        btInputStream  = btInputStream_t;
    }

    public class ReceiveThread extends Thread{
        ShareBuffer shareBuffer;

        public ReceiveThread(){
            shareBuffer = Auth.shareBuffer2;
        }

        public void run(){
            try {
                while (true)
                {
                    int bytesAvailable = reader.available();
                    if(!shareBuffer.isfull(bytesAvailable) && bytesAvailable!=0) {
                        /** format and save*/
//                        Log.i("socket_read", String.format("%d", bytesAvailable));
                        shareBuffer.writeToBuffer(reader, bytesAvailable);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            try {
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

//    public String sendFrame(String patient_phone_num, String doctor_phone_num, String content){
//        SimpleDateFormat sTimeFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        map.put("datetime", sTimeFormat.format(new Date()));
//        return frame;
//    }
    public void setSenderAndReceiver(String s, String r){
        this.s = s;
        this.r = r;
    }

    private void mySuspend(){
        suspendFlag = true;
    }

    public synchronized void myResume(){
        suspendFlag = false;
        notify();
    }

    public void run() {
//        try {
        try {
            InetAddress addr = InetAddress.getByName("www.xiaopeng.site");
//            byte[] bs = new byte[] { (byte)192, (byte)168, 31, (byte)230};
//            InetAddress addr = InetAddress.getByAddress(bs);
            socket = new Socket(addr, PORT);
        }
        catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e2) {
            }
        }

        s = AppStatus.getUserid();
        r = AppStatus.getTargetId();

        try {
//            in = new BufferedReader(new InputStreamReader(
//                    socket.getInputStream()));
//            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//                    socket.getOutputStream())), true);

            OutputStream outStream = socket.getOutputStream();
            outStream.write(String.format("%s&%s", s, r).getBytes());

            InputStream inStream = socket.getInputStream();
            byte[] message = new byte[20];
            int counter = inStream.read(message);
            String line = new String(message, 0, counter);

            if(line.equals("True")) {
                Auth.AUTH_SUCCESS = true;
            }
            else{
                outStream.flush();
                socket.close();
                return;
            }

            writer = new BufferedOutputStream(socket.getOutputStream(), 16*1024);
            reader = new BufferedInputStream(socket.getInputStream(), 16*1024);

            /**/
//            ReceiveThread receiveThread = new ReceiveThread();
//            receiveThread.start();

            while(true) {
                if(!shareBuffer.isempty())
                {
                    shareBuffer.readFromeBuffer(writer);
                    writer.write("\n".getBytes());
//                    Log.i("send_to_network buffer1", String.format("length:%d", shareBuffer.getLength()));
                }
                int bytesAvailable = reader.available();
                if(!Auth.shareBuffer2.isfull(bytesAvailable) && bytesAvailable!=0) {
                    /** format and save*/
//                        Log.i("socket_read", String.format("%d", bytesAvailable));
                    Auth.shareBuffer2.writeToBuffer(reader, bytesAvailable);
                    Log.i("recv_from_network buffer2", String.format("num:%d, length:%d", bytesAvailable, Auth.shareBuffer2.getLength()));
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            cancel();
        }
    }

    public void cancel(){
        try {
            if(writer != null) {
                writer.flush();
                writer.close();
            }
            if(socket != null) {
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

