package com.uestcpg.remotedoctor.Bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by John on 2017/8/4.
 */

public class ShareBuffer {
    private final int AMOUNT    = 400;
    private final int ONETIMELENG = 1000;
    private int amount;
    private byte[] buffer;

    private int front;
    private int behind;
    private boolean isFlushed;

    private int counter;

    public ShareBuffer(){
        buffer = new byte[AMOUNT*ONETIMELENG];
        amount = AMOUNT* ONETIMELENG;

        reset();
    }

    public void reset(){
        front = 0;
        behind = 0;
        counter = 0;
    }

    public int getLength(){
        return (behind - front + amount) % amount;
    }

    public synchronized boolean isfull(int length){
        return (behind - front + amount) % amount >= amount - length;
    }

    public synchronized boolean isempty(){
        return front == behind;
    }

    public synchronized boolean isempty(int length){
        return (behind - front + amount) % amount <= length;
    }

    public synchronized void writeToBuffer(InputStream mmInStream, int length) throws IOException{
//            System.arraycopy(frames, 0, buffer, behind, length);
//        Log.i("write", String.format("length:%d, front:%d, behind%d", length, front, behind));
        if(length == 0)
            return;

        if (behind + length > amount) {
            mmInStream.read(buffer, behind, amount - behind);
            mmInStream.read(buffer, 0, length - amount + behind);
        }
        else{
            mmInStream.read(buffer, behind, length);
        }
//        try {
//            Thread.sleep(10);// 延迟
//        } catch (InterruptedException e) {
////                 TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        behind += length;
        behind = behind % amount;
    }

    public synchronized int readFromeBuffer(OutputStream mmOutStream) throws IOException{

        int current_length = (behind - front + amount) % amount;
//        Log.i("read", String.format("length:%d, front:%d, behind%d",current_length, front, behind));

        if (behind < front) {
            mmOutStream.write(buffer, front, amount - front);
            mmOutStream.write(buffer, 0, current_length - amount + front);
        }
        else{
            mmOutStream.write(buffer, front, current_length);
        }

//            if((counter % 100) == 0)
//                mmOutStream.flush();

//            counter++;
        mmOutStream.flush();
//        if(!isFlushed && front > 100*1000) {

//            isFlushed = true;
//        }
//
//        if(isFlushed && front < 100*1000) {
//            isFlushed = false;
//        }

//            try {
//                Thread.sleep(50);// 延迟
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

        front += current_length;
        front = front % amount;
        return current_length;
    }

    public synchronized int readAmountFromeBuffer(OutputStream mmOutStream, int length) throws IOException{

        int current_length = length;
//        Log.i("read", String.format("length:%d, front:%d, behind%d",current_length, front, behind));
        if(length == 0)
            return 0;

        if (front + current_length > amount) {
            mmOutStream.write(buffer, front, amount - front);
            mmOutStream.write(buffer, 0, current_length - amount + front);
        }
        else{
            mmOutStream.write(buffer, front, current_length);
        }

//            if((counter % 100) == 0)
//                mmOutStream.flush();

//            counter++;

        mmOutStream.flush();
//        if(!isFlushed && front > 100*1000) {
//            mmOutStream.flush();
//            isFlushed = true;
//        }

//        if(isFlushed && front < 100*1000) {
//            isFlushed = false;
//        }

//            try {
//                Thread.sleep(50);// 延迟
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

        front += current_length;
        front = front % amount;
        return current_length;
    }
}