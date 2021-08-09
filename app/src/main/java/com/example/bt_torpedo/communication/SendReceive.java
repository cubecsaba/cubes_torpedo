package com.example.bt_torpedo.communication;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.bt_torpedo.game.MyHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


import static com.example.bt_torpedo.entry.BT_Activity.STATE_MESSAGE_RECEIVED;


public class SendReceive extends Thread {

    private BluetoothSocket bluetoothSocket;  // "final" deleted
    private Socket netSocket; // "final" deleted
    private final InputStream inputStream;
    private final OutputStream outputStream;

    // constructor for bluetooth communication
    public SendReceive(BluetoothSocket socketBT) {
        bluetoothSocket = socketBT;
        InputStream tempIn = null;
        OutputStream tempOut = null;
        try {
            tempIn = bluetoothSocket.getInputStream();
            tempOut = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = tempIn;
        outputStream = tempOut;
    }
// constructor for network communication
    public SendReceive(Socket socket) {
        netSocket = socket;
        InputStream tempIn = null;
        OutputStream tempOut = null;
        try {
            tempIn = netSocket.getInputStream();
            tempOut = netSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = tempIn;
        outputStream = tempOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        Handler handler = new MyHandler(Looper.getMainLooper());


        while (true) {
            try {
                bytes = inputStream.read(buffer);
                //handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                Message message = Message.obtain();
                message.what = STATE_MESSAGE_RECEIVED;
                message.arg1 = bytes;
                message.arg2 = -1;
                message.obj = buffer;
                handler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

