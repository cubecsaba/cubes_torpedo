package com.example.bt_torpedo;

public class Net_Connect extends Thread {

    /*public static PrintWriter output;
    public static BufferedReader input;
    public Socket socketNET;
    private ReadNet readNet;
    public boolean interrupt = false;


    public void run() {

        try {
            socketNET = new Socket(SERVER_IP, SERVER_PORT); //try to connect
            output = new PrintWriter(socketNET.getOutputStream());  //assign writer to the socket's output stream
            input = new BufferedReader(new InputStreamReader(socketNET.getInputStream()));  //assign reader to the socket's input stream
            output.write(etUser.getText() + ":1234" + "\r\n"); // User name and hardcoded pw transmitted
            output.flush();
            sendReceive = new SendReceive(socketNET);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnDisConnect.setEnabled(true); //makes EXIT possible
                    btnConnect.setEnabled(false);  //does not allow new connection (until EXIT)
                }
            });

            //*****************
            //sendReceive.start(); //start communication thread for TorpedoActivity
            //startActivityForResult(intent, CONT); // start TorpedoActivity
            //new Thread(new ReadLine()).start(); // start a ReadLine thread
            //*****************

            readNet = new ReadNet();
            readNet.run();


            while (!interrupt) {
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
