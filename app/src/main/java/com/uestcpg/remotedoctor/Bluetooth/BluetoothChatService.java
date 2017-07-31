/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uestcpg.remotedoctor.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.uestcpg.remotedoctor.Bluetooth.common.logger.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

/**
 * 本类实现了蓝牙启动、管理、连接到其他设备的所有功能
 * 他创建了一个线程来监听连接请求，创建一个线程来连接其它设备，创建一个线程处理连接后要收发的数据
 */
public class BluetoothChatService {
    // Debugging
    private static final String TAG = "BluetoothChatService";

    // 当创建服务器Socket时，为SDP记录命名
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";
//
    // 为应用设置唯一的UUID
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // Unique UUID for this application
//    private static final UUID MY_UUID_SECURE =
//            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
//    private static final UUID MY_UUID_INSECURE =
//            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    // 定义一个蓝牙适配器
    private final BluetoothAdapter mAdapter;

    // 定义一个Handler，接收子线程发送的数据，并配合主线程更新UI
    private final Handler mHandler;

    // 定义一个Handler，从接收线程发送数据到发送线程

    // 定义一个用于安全的接收数据的线程
    private AcceptThread mSecureAcceptThread;

    // 定义一个不安全的接收数据的线程
    private AcceptThread mInsecureAcceptThread;

    // 定义一个用于连接设备的线程
    private ConnectThread mConnectThread;

    // 定义一个用于处理连接后数据收发的线程
    private ConnectedThread mConnectedThread;
    private ConnectedThread mConnectedThread_r;
    private ConnectedSendThread mConnectedSendThread;
    private ConnectedSendThread mConnectedSendThread_r;

    private ShareBuffer shareBuffer1;
    private ShareBuffer shareBuffer2;

    // 定义一个状态变量
    private int mState;

    // 定义一个状态更新变量
    private int mNewState;

    private FileManager fileManager;

    private String filename;

    private boolean isReady;
    private BluetoothSocket current_sock;
    private String current_socketType;

    // Constants that indicate the current connection state声明表示当前连接状态的常量
    public static final int STATE_NONE = 0;       // 未做任何操作用0表示
    public static final int STATE_LISTEN = 1;     // 监听接入时用1表示
    public static final int STATE_CONNECTING = 2; // 监听外连时用2表示
    public static final int STATE_CONNECTED = 3;  // 当已连接其他设备时用3表示

    /**
     * 构造一个新的BluetoothChat Session
     *
     * @param context 包含了UI Acitvity的Context
     * @param handler 用于向UI Activity返回消息
     */
    public BluetoothChatService(Context context, Handler handler) {
        // 初始化蓝牙适配器
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        // 设置当前的状态为“无操作”
        mState = STATE_NONE;

        // 更新状态也为“无操作”
        mNewState = mState;

        // UI更新器初始化
        mHandler = handler;


//        SimpleDateFormat sTimeFormat= new SimpleDateFormat("_yyyy_MM_dd_hh_mm_ss");
//        String date = sTimeFormat.format(new Date());
//        filename = "recv" + date + ".txt";
        filename = "recv123" + ".txt";

        fileManager = new FileManager(context);

        shareBuffer1 = new ShareBuffer();
        shareBuffer2 = new ShareBuffer();
        isReady = false;
//        try {
//            fileManager.writeToFile();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

//        FileManager fileManager2 = new FileManager(context, "abc");
//        mHandler.obtainMessage(Constants.MESSAGE_READ, fileManager, -1, buffer).sendToTarget();
//        fileManager2.addOneLine("abc");
//        try {
//            fileManager2.writeToFile();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    /**
     * 根据当前的连接状态，更新UI标题
     */
    private synchronized void updateUserInterfaceTitle() {
        // 获取当前的状态
        mState = getState();
        Log.d(TAG, "updateUserInterfaceTitle() " + mNewState + " -> " + mState);

        // 更新状态
        mNewState = mState;

        // 将新的状态值传递给Handler，它才可以更新UI
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mNewState, -1).sendToTarget();
    }

    /**
     * 返回当前连接状态
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * 开启聊天服务，同时开启接收线程，监听服务器模式
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // 取消所有尝试连接设备的线程
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // 取消当前已运行的连接设备的进程
        if (mConnectedThread != null && mConnectedSendThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
            /**/
            mConnectedSendThread_r.cancel();
            mConnectedSendThread = null;
        }

        // 开始监听BluetoothServerSocket的线程
        if (mSecureAcceptThread == null) {
            mSecureAcceptThread = new AcceptThread(true);
            mSecureAcceptThread.start();
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread(false);
            mInsecureAcceptThread.start();
        }
        // 更新UI中的标题
        updateUserInterfaceTitle();
    }

    /**
     * 开启连接线程，初始化一个连接到远程设备
     *
     * @param device 要连接的设备
     * @param secure 套接字的安全类型 - 安全 (true) , 不安全 (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null && mConnectedSendThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
            /**/
            mConnectedSendThread.cancel();
            mConnectedSendThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();

        // Update UI title
        updateUserInterfaceTitle();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null && mConnectedSendThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
            /**/
            mConnectedSendThread.cancel();
            mConnectedSendThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        if(!isReady) {
            current_sock = socket;
            current_socketType = socketType;
            isReady      = true;
//            mConnectedThread = new ConnectedThread(socket, socketType);
        }
        else{
            mConnectedThread = new ConnectedThread(socket, socketType, shareBuffer1);
            mConnectedSendThread_r = new ConnectedSendThread(current_sock, current_socketType, shareBuffer1);

            mConnectedThread_r = new ConnectedThread(current_sock, current_socketType, shareBuffer2);
            mConnectedSendThread = new ConnectedSendThread(socket, socketType, shareBuffer2);

            mConnectedThread.start();
            mConnectedSendThread.start();
            mConnectedThread_r.start();
            mConnectedSendThread_r.start();
        }

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // Update UI title
        updateUserInterfaceTitle();
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null && mConnectedSendThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
            /**/
            mConnectedSendThread.cancel();
            mConnectedSendThread = null;
        }

        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }

        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }
        mState = STATE_NONE;
        // Update UI title
        updateUserInterfaceTitle();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedSendThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedSendThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        // Update UI title
        updateUserInterfaceTitle();

        // Start the service over to restart listening mode
        BluetoothChatService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        // Update UI title
        updateUserInterfaceTitle();

        // Start the service over to restart listening mode
        BluetoothChatService.this.start();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Create a new listening server socket
            try {
                if (secure) {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
                            MY_UUID_SECURE);
                } else {
                    tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                            NAME_INSECURE, MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
            }
            mmServerSocket = tmp;
            mState = STATE_LISTEN;
        }

        public void run() {
            Log.d(TAG, "Socket Type: " + mSocketType +
                    "BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothChatService.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice(),
                                        mSocketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
        }

        public void cancel() {
            Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
            }
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(
                            MY_UUID_SECURE);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(
                            MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
            mState = STATE_CONNECTING;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
        private final int FRAMECOUNTER = 10;

        private int frame_counter;
        private int last_frame_counter;

        private Timer store_timer;
        private Timer plot_timer;

        private MyTask myTask;
        private PlotTask plotTask;

        private String content;
        private ShareBuffer shareBuffer;

        private int zeros_counter;

        public ConnectedThread(BluetoothSocket socket, String socketType, ShareBuffer sharebuffer) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;

//            frame_counter = 0;
//            last_frame_counter = frame_counter;
//            content = "";

//            store_timer = new Timer();
//            plot_timer  = new Timer();
//
//            myTask      = new MyTask();
//            plotTask    = new PlotTask();
//
//            store_timer.scheduleAtFixedRate(myTask, 0, 5000);
//            plot_timer.scheduleAtFixedRate(plotTask, 0, 100);
            shareBuffer = sharebuffer;
            zeros_counter = 0;
        }

        public void reset()
        {
            try {
                mmInStream.reset();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
         }

        public void frame_gotten(int readBufferPosition, byte[] buffer)
        {
            frame_counter += 1;

//            msg = mSendHander.obtainMessage();
//            msg.obj = buffer;
//            mSendHander.sendMessage(msg);
//            if (!shareBuffer.isfull())
//                shareBuffer.writeToBuffer(buffer);

//            mHandler.obtainMessage(Constants.MESSAGE_READ, readBufferPosition, -1, buffer).sendToTarget();
            StringBuilder line = StringHexUtils.getHexByte(buffer, 0, readBufferPosition);

            content += line.toString();
            content += "\n";
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
//            byte[] buffer = new byte[20000];
//            int buffer_cur_pos = 0;
//
//            int starter = 0x55;
//            int ender   = -86;
//
//            boolean isStarted = false;
//            boolean isEnded   = false;
//            int byteCounter  = 0;
//
//            byte[] frame  = new byte[10];

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    int bytesAvailable = mmInStream.available();
                    if(zeros_counter == 10) {
                        reset();
                        zeros_counter = 0;
                    }

                    if(bytesAvailable > 0)
                    {
                        if(!shareBuffer.isfull(bytesAvailable))
                        {
                            shareBuffer.writeToBuffer(mmInStream, bytesAvailable);
                        }
//                        byte[] packetBytes = new byte[bytesAvailable];
//                        mmInStream.read(buffer);
                        // str = "55 01 02 03 04 05 06 aa 12 58"
                        // str = "55 00 00 00 00 00 00 aa 11 59"

//                        for(int i=0;i<bytesAvailable;i++)
//                        {
//                            byte b = packetBytes[i];
//                            if(b == starter)
//                                isStarted = true;
//
//                            if(isStarted && byteCounter < FRAMECOUNTER - 3) {
//                                frame[byteCounter] = b;
//                            }
//
//                            if(b == ender && byteCounter == FRAMECOUNTER - 3)
//                                isEnded = true;
//
//                            /*遇不到结尾，那么就不要前面的字节，重新找开始字节*/
//                            if(b != ender && byteCounter == FRAMECOUNTER - 3) {
//                                isStarted = false;
//                                byteCounter = 0;
//                            }
//
//                            if(isEnded && byteCounter < FRAMECOUNTER){
//                                frame[byteCounter] = b;
//                                if (byteCounter == FRAMECOUNTER - 1)
//                                {
//                                    byteCounter = 0;
//                                    System.arraycopy(frame, 0, buffer, buffer_cur_pos, FRAMECOUNTER);
//                                    buffer_cur_pos += FRAMECOUNTER;
//                                    if(buffer_cur_pos == 100*FRAMECOUNTER) {
//                                        frame_gotten(buffer_cur_pos, buffer);
//                                        write(buffer);
//                                        buffer_cur_pos = 0;
//                                    }
//                                }
//                            }
//                            if(isStarted)
//                                byteCounter += 1;
//                        }
//                    }
                        zeros_counter = 0;
                    }
                    else {
                        zeros_counter++;
                    }
//                                if(CRC16.isCrcSuccessed(encodedBytes)) {
//                                    content += line.toString();
//                                    int xs = StringHexUtils.bytes_process(encodedBytes[1], encodedBytes[2]);
//                                    StringHexUtils.bytes_process(encodedBytes[3], encodedBytes[4]);
//                                    StringHexUtils.bytes_process(encodedBytes[5], encodedBytes[6]);
                                }
                catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }
//        public void run() {
//            Log.i(TAG, "BEGIN mConnectedThread");
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            // Keep listening to the InputStream while connected
//            while (mState == STATE_CONNECTED) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//                    // Send the obtained bytes to the UI Activity
//                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
//
//                    msg = mSendHander.obtainMessage();
//                    msg.obj = buffer;
//                    mSendHander.sendMessage(msg);
//
//                } catch (IOException e) {
//                    Log.e(TAG, "disconnected", e);
//                    connectionLost();
//                    break;
//                }
//            }
//        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         **/

        public void write(byte[] buffer) {
//            try {
//                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, 10, -1, buffer)
                        .sendToTarget();
//            } catch (IOException e) {
//                Log.e(TAG, "Exception during write", e);
//            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
//            try {
//                fileManager.writeToFile();
//            } catch (Exception e){
//                Log.e(TAG, "writeToFile failed", e);
//            }
        }

        class MyTask extends java.util.TimerTask{
            @Override
            public void run(){
                // TODO Auto-generated method stub
                if(last_frame_counter == frame_counter && frame_counter > 0)
                {
                    SimpleDateFormat sTimeFormat= new SimpleDateFormat("_yyyy_MM_dd_hh_mm_ss");
                    String date = sTimeFormat.format(new Date());
                    filename = String.format("_recv_%d", frame_counter) + date + ".txt";
                    File file = fileManager.createFile(filename);
                    try {
                        fileManager.writeToFile(file, content);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    content = "";
                    frame_counter = last_frame_counter = 0;
                }
                last_frame_counter = frame_counter;
            }
        }

        class PlotTask extends java.util.TimerTask{
            @Override
            public void run(){
                int[] xs;
                float[] ys;
            }
        }
    }

    private class ConnectedSendThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private ShareBuffer shareBuffer;

        public ConnectedSendThread(BluetoothSocket socket, String socketType, ShareBuffer sharebuffer) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;

            shareBuffer = sharebuffer;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
//            byte[] buffer = new byte[1024];
//            int bytes;
//            Looper.prepare();//准备Looper对象
//            //在分线程中实现mSendHander，就会在分线程中处理其msg
//            mSendHander = new Handler(){//这是用匿名内部类生成一个handler对象
//                public void handleMessage(Message msg) {
//                    write((byte[])msg.obj);
//                }
//            };
//            //调用Looper的loop方法后，Looper对象将不断从消息队列中取出消息对象并交给handleMessage处理
//            //没有消息时该线程会阻塞
//            Looper.loop();
            while (mState == STATE_CONNECTED){
                if(!shareBuffer.isempty())
                {
                    shareBuffer.readFromeBuffer(mmOutStream);
                }
            }
        }
        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         **/

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, 10, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
//            try {
//                fileManager.writeToFile();
//            } catch (Exception e){
//                Log.e(TAG, "writeToFile failed", e);
//            }
        }
    }

    class ShareBuffer{
        private final int AMOUNT    = 1;
        private final int ONETIMELENG = 1000;
        private int amount;
        private byte[] buffer;

        private int front;
        private int behind;

        public ShareBuffer(){
            buffer = new byte[AMOUNT*ONETIMELENG];
            amount = AMOUNT* ONETIMELENG;

            front = 0;
            behind = 0;
        }

        public synchronized boolean isfull(int length){
            return (front - length) % amount == behind;
        }

        public synchronized boolean isempty(){
            return front == behind;
        }

        public synchronized void writeToBuffer(InputStream mmInStream, int length){
//            System.arraycopy(frames, 0, buffer, behind, length);
            try {
                if (behind + length > amount) {
                    mmInStream.read(buffer, behind, amount - behind);
                    mmInStream.read(buffer, 0, length - amount + behind);
                }
                else{
                    mmInStream.read(buffer, 0, length);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            behind += length;
            behind = behind % amount;
        }

        public synchronized void readFromeBuffer(OutputStream mmOutStream){
            int current_length = (behind - front) % amount;
            try {
                if (behind < front) {
                    mmOutStream.write(buffer, front, amount - front);
                    mmOutStream.write(buffer, 0, current_length - amount + front);
                }
                else{
                    mmOutStream.write(buffer, front, current_length);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            front += current_length;
            front = front % amount;
        }
    }

}
