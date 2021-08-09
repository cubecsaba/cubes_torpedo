package com.example.bt_torpedo.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt_torpedo.views.MyArrayAdapter;
import com.example.bt_torpedo.R;
import com.example.bt_torpedo.communication.SendReceive;
import com.example.bt_torpedo.game.TorpedoActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.example.bt_torpedo.entry.StartActivity.sendReceive;

public class NET_Activity extends AppCompatActivity {

    Thread thConnect = null;
    EditText etIP, etPort, etUser;
    public static TextView tvMessage, tvPartner;
    ListView lvUsers;
    Button btnDisConnect, btnConnect;
    public static String SERVER_IP;
    String user_i;
    public static List<String> usersList;
    Intent intent;
    public static int SERVER_PORT;
    public static PrintWriter output;
    public static BufferedReader input;
    public static MyArrayAdapter arrayAdapter;
    public String partner = "";
    public boolean interrupt = false;
    public Socket socketNET;
    private String[] piecesOfMessage;
    private static final int CONT = 1;
    private ReadLine readLine;
    static final int NET_CONNECTION_ESTABLISHED = 2;
    static final int ULIST = 3;
    static final int DEL_ULIST = 4;
    static final int ALONE = 5;
    static final int PARTNER = 6;
    private boolean connected = false;
    public static boolean partnered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net);
        findViews();
        btnDisConnect.setEnabled(false);
        usersList = new ArrayList<>();
        arrayAdapter = new MyArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, usersList);
        lvUsers.setAdapter(arrayAdapter);
        intent = new Intent(this, TorpedoActivity.class);
        listenerStart();
    }

    void findViews() {
        etIP = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        etUser = findViewById(R.id.etUSer);
        tvMessage = findViewById(R.id.tvMessage);
        tvPartner = findViewById(R.id.tvPartner);
        lvUsers = findViewById(R.id.lvUsers);
        btnDisConnect = findViewById(R.id.btnDisConnect);
        btnConnect = findViewById(R.id.btnConnect);
    }

    void listenerStart() {
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //etUser.setEnabled(false); // user name is fixed
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
                thConnect = new Thread(new Connection());  //  instantiates Connection Thread
                thConnect.start();  // starts thConnect
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!connected) {  // if WiFi connection does not get completed in 2 sec, then send a message to check if on the same rooter.
                    tvMessage.setText("Server not reached.\n\rIf on WiFi, then the same rooter to be used!");
                }
            }
        });

        btnDisConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new WriteLine("EXIT")).start(); // sends EXIT message to the server
                btnDisConnect.setEnabled(false); // reinitializes for a new connection
                btnConnect.setEnabled(true);
                arrayAdapter.clear();
                etUser.setEnabled(true);
                etUser.setText("");
                tvMessage.setText("");
                tvPartner.setText("");
            }
        });

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                partner = usersList.get(i);
                new Thread(new WriteLine("PARTNER:" + partner + ":" + etUser.getText() + "\r\n")).start(); //sends the name of selected partner to the server

            }
        });
    }

    Handler handlerNET = new Handler(new Handler.Callback() {  // interacts between UI and Connection Thread
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case NET_CONNECTION_ESTABLISHED:
                    btnDisConnect.setEnabled(true); //makes EXIT possible
                    btnConnect.setEnabled(false);  //does not allow new connection (until EXIT)
                    etUser.setEnabled(false); // user name is fixed
                    connected = true;
                    break;
                case ULIST:
                    usersList.add(user_i); // update users' list
                    arrayAdapter.notifyDataSetChanged(); //update lvUsers
                    tvMessage.setText("Select the user, you want to play with:");
                    break;
                case DEL_ULIST:
                    if (tvPartner.getText() == user_i) { //delete if it was the PARTNER
                        tvPartner.setText("");
                    }
                    usersList.remove(user_i); //remove user from the users' list
                    arrayAdapter.notifyDataSetChanged(); //update lvUsers
                    break;
                case ALONE:
                    tvMessage.setText("Currently no other user logged on."); // nobody else is connected
                    break;
                case PARTNER:
                    tvPartner.setText(piecesOfMessage[1]); // displays selected partner
                    partnered = true;
            }
            return true;
        }
    });

    // creates NETwork connection, starts communication thread
    class Connection implements Runnable {
        public void run() {

            try {
                socketNET = new Socket(SERVER_IP, SERVER_PORT); //try to connect
                output = new PrintWriter(socketNET.getOutputStream());  //assign writer to the socket's output stream
                input = new BufferedReader(new InputStreamReader(socketNET.getInputStream()));  //assign reader to the socket's input stream
                output.write(etUser.getText() + ":1234" + "\r\n"); // User name and hardcoded pw transmitted
                output.flush();
                sendReceive = new SendReceive(socketNET); // instantiate SendReceive for continouos socket gaming communication
                Message message = Message.obtain();
                message.what = NET_CONNECTION_ESTABLISHED; // prepare message
                handlerNET.sendMessage(message);  // send message to UI

                //*****************
                //sendReceive.start(); //start communication thread for TorpedoActivity
                //startActivityForResult(intent, CONT); // start TorpedoActivity
                //new Thread(new ReadLine()).start(); // start a ReadLine thread
                //*****************

                readLine = new ReadLine(); //create and start communication thread to communicate between this client and the server
                readLine.run(); // this channel is used for basic connection communication, not for transfering game related data
                while (!interrupt) { //abandon communication thread when the client exits
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ReadLine implements Runnable { //  // thread for receiving information: takes input stream`s content and notfies UI through handlerNET
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine(); // reads from the Connection`s input
                    // depending on the message:
                    if (message != null && message.startsWith("ULIST:")) { //notification about new user's logon
                        user_i = message.substring(6); // takes the content of the message from the 6th character
                        Message hint = Message.obtain(); // prepares message to forward to UI
                        hint.what = ULIST;
                        handlerNET.sendMessage(hint);

                    } else if (message != null && message.startsWith("DelULIST:")) { // notification about logoff of a user
                        user_i = message.substring(9); // takes the content of the message from the 9th character
                        Message hint = Message.obtain();
                        hint.what = DEL_ULIST;
                        handlerNET.sendMessage(hint);

                    } else if (message != null && message.startsWith("ALONE:")) { // in case no other user logged on on the server
                        Message hint = Message.obtain();
                        hint.what = ALONE;
                        handlerNET.sendMessage(hint);

                    } else if (message != null && message.startsWith("PARTNER:")) { // in case message starts with "PARTNER"
                        Message hint = Message.obtain();
                        hint.what = PARTNER;
                        piecesOfMessage = message.split(":"); // splits message, Index Nr1 will be displayed as "Partner" on UI
                        handlerNET.sendMessage(hint);

                    } else if (message != null && message.startsWith("LIFE")) { // in case message starts with "LIFE"
                        new Thread(new WriteLine("LIFE\r\n")).start(); //send sign of LIFE to the server to mirror server's message for watchdog purposes
//*************************************
                    } else { // to be adapted
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //tvMessages.append(message + "\n");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class WriteLine implements Runnable { // thread for sending information
        private String message;

        //constructor
        WriteLine(String message) {
            this.message = message;
        }

        // !!!!!!!!! to be adjusted!!!!!!!!
        @Override
        public void run() {

            /*if (!partner.equals("") && !message.equals("EXIT") && message.startsWith("PARTNER")) {// in case message starts with "PARTNER"
                output.write(message);
                output.flush();
                partner = "";  // reset addressee
            } else */
            if (!message.equals("EXIT")) {  // if not EXIT
                output.write(message + "\r\n");
                output.flush();
            } else {  // if message is EXIT
                output.write(message + "\r\n"); // send message
                output.flush();
                interrupt = true; // break out of while loop of Connection thread -> abandon that thread
            }
        }
    }

    // !!!!!!!!! to be adjusted!!!!!!!!  continue activity upon completion of other activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result;

            if (requestCode == CONT) {   // identifies the intent
                result = data.getStringExtra("IsNewGame?");

                if (result.equals("Continue")) {        // start TorpedoActivity again if to be continued
                    Intent intent1 = new Intent(this, TorpedoActivity.class);
                    startActivityForResult(intent1, CONT);
                } else if (result.equals("Finish")) {   //exit application
                    finishAffinity();
                }
            }
        }
    }

}
