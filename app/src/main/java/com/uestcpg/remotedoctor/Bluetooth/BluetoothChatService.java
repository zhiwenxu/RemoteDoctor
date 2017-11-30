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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.uestcpg.remotedoctor.Bluetooth.FileManager;
import com.uestcpg.remotedoctor.Bluetooth.socket.MultiClient;
import com.uestcpg.remotedoctor.Bluetooth.socket.SocketClientThread;

import com.uestcpg.remotedoctor.R;

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

    //    private static final UUID MY_UUID_SECURE =
//            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
//    private static final UUID MY_UUID_INSECURE =
//            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    // 为应用设置唯一的UUID
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // 定义一个蓝牙适配器
    private final BluetoothAdapter mAdapter;

    // 定义一个Handler，接收子线程发送的数据，并配合主线程更新UI
    private final Handler mHandler;

    private BluetoothDevice cr_device;

    // 定义一个用于安全的接收数据的线程
    private AcceptThread mSecureAcceptThread;

    // 定义一个不安全的接收数据的线程
    private AcceptThread mInsecureAcceptThread;

    // 定义一个用于连接设备的线程
    private ConnectThread mConnectThread;

    // 定义一个用于处理连接后数据收发的线程
    public ConnectedThread mConnectedThread;

    // 定义一个状态变量
    private int mState;

    // 定义一个状态更新变量
    private int mNewState;

    private FileManager fileManager;

    private String filename;

    private ShareBuffer shareBuffer;

    // Constants that indicate the current connection state声明表示当前连接状态的常量
    public static final int STATE_NONE = 0;       // 未做任何操作用0表示
    public static final int STATE_LISTEN = 1;     // 监听接入时用1表示
    public static final int STATE_CONNECTING = 2; // 监听外连时用2表示
    public static final int STATE_CONNECTED = 3;  // 当已连接其他设备时用3表示

    private BlockingQueue basket;
    private SocketClientThread socket_client;

    private byte[] frame;
    private byte[] heheda;
    private int PACKET_SIZE;
    /**
     * 构造一个新的BluetoothChat Session
     *
     * @param context 包含了UI Acitvity的Context
     * @param handler 用于向UI Activity返回消息
     */
    public BluetoothChatService(Context context, Handler handler, BlockingQueue basket,
                                SocketClientThread socket_client) {
        // 初始化蓝牙适配器
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        // 设置当前的状态为“无操作”
        mState = STATE_NONE;

        // 更新状态也为“无操作”
        mNewState = mState;

        // UI更新器初始化
        mHandler = handler;
        this.basket = basket;
        this.socket_client = socket_client;

//        SimpleDateFormat sTimeFormat= new SimpleDateFormat("_yyyy_MM_dd_hh_mm_ss");
//        String date = sTimeFormat.format(new Date());
//        filename = "recv" + date + ".txt";
        filename = "recv123" + ".txt";


        fileManager = new FileManager(context,filename);

        frame = new byte[10];
        String str = "55 00 00 00 00 57 07 AA B6 97";
        String[] items = str.trim().split(" ");

        for(int i=0; i < items.length; i++)
        {
            frame[i] = StringHexUtils.generateByte(items[i]);
        }

        PACKET_SIZE = 100;
        heheda = new byte[PACKET_SIZE];
        for(int j =0; j<PACKET_SIZE / 10; j++)
        {
            System.arraycopy(frame, 0, heheda, j*10, 10);
        }

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

    public void initSharedBuffer(ShareBuffer shareBuffer1) {
        shareBuffer = shareBuffer1;
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
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
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
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
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
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
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
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

//        ConnectedSendThread sendThread = new ConnectedSendThread(mConnectedThread.getWriter(), mConnectedThread.mmOutStream);
//        sendThread.start();
//        cr_device = device;
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

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
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
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
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
//                    tmp = device.createRfcommSocketToServiceRecord(
//                            MY_UUID_SECURE);
                    try {
                        Method method = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                        tmp = (BluetoothSocket) method.invoke(device, 1);
                    } catch (Exception e) {
                        Log.e(TAG, "No such method.", e);
                    }
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
    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private boolean isClose = false;
        private BufferedInputStream reader = null;
        private BufferedOutputStream writer = null;
        private ShareBuffer shareBuffer;
        private Timer timer = new Timer();

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                reader = new BufferedInputStream(tmpIn, 16 * 1024);
                writer = new BufferedOutputStream(tmpOut, 16 * 1024);
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;

            shareBuffer = Auth.shareBuffer1;
        }

        public BufferedOutputStream getWriter(){
            return writer;
        }

        public void Close(){
            isClose = true;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            int bytesAvailable;
//            byte[] packages = new byte[150];

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED && !isClose && Auth.AUTH_SUCCESS) {
                try {
                    bytesAvailable = reader.available();
                    if (!shareBuffer.isfull(bytesAvailable)) {
                        shareBuffer.writeToBuffer(reader, bytesAvailable);
//                        Log.i("write_to_network buffer1", String.format("%d, Length:%d", bytesAvailable, shareBuffer.getLength()));
                    }
                    if (!Auth.shareBuffer2.isempty(bytesAvailable) && bytesAvailable > 0){
                        int tmp = Auth.shareBuffer2.readAmountFromeBuffer(writer, bytesAvailable);
                        Log.i("send_to_bluetooth buffer2", String.format("%d, Length:%d", bytesAvailable, Auth.shareBuffer2.getLength()));
                    }
                } catch (Exception e) {
                    Log.e(TAG, " there is a exception disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }
        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
        //         */

        public void cancel() {
            if (mmInStream != null) {
                try {
                    mmInStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                    mmInStream = null;
            }
            if (mmOutStream != null) {
                try {
                    mmOutStream.flush();
                    mmOutStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                    mmOutStream = null;
            }

            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }

//            Message msg = mHandler.obtainMessage(Constants.RESTART);
//            mHandler.sendMessage(msg);
        }

        public void write(byte[] bytes){
            return;
        }
    }

    private class ConnectedSendThread extends Thread{
        private BufferedOutputStream writer;
        private ShareBuffer shareBuffer;
        private OutputStream mmOutStream;

        public ConnectedSendThread(BufferedOutputStream writer, OutputStream mmOutStream){
            this.writer = writer;
            this.shareBuffer = Auth.shareBuffer2;
            this.mmOutStream = mmOutStream;
        }

        public void run(){
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//                        byte[] tmp = new byte[]{0x55, 0, 0, 0, 0, 0, 0, -86, 0, 0};
//                        writer.write(tmp);
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 0, 20);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (!shareBuffer.isempty(100)){
                            int tmp = shareBuffer.readAmountFromeBuffer(writer, 100);
                            Log.i("sendtobluetooth", String.format("%d", tmp));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, " there is a exception disconnected", e);
//                    connectionLost();
//                        break;
                    }
                }
            }, 0, 10);

//
//           new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//                        writer.flush();
//                    } catch (Exception e) {
//                        Log.e(TAG, " there is a exception disconnected", e);
////                    connectionLost();
////                        break;
//                    }
//                }
//            }, 0*1000, 1*1000);

//            while (mState == STATE_CONNECTED)
//            {
//                try {
//                    if (!shareBuffer.isempty()){
//                        int tmp = shareBuffer.readFromeBuffer(writer);
//                        writer.flush();
//                        Log.i("sendtobluetooth", String.format("%d", tmp));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e(TAG, " there is a exception disconnected", e);
////                    connectionLost();
//                    break;
//                }
//            }
        }
    }
}
