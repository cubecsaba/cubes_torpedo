package com.example.bt_torpedo.entry;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.bt_torpedo.options.ListItemAdapter;
import com.example.bt_torpedo.R;
import com.example.bt_torpedo.communication.SendReceive;
import com.example.bt_torpedo.options.SettingsActivity;
import com.example.bt_torpedo.game.TorpedoActivity;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import static com.example.bt_torpedo.entry.StartActivity.bluetoothAdapter;
import static com.example.bt_torpedo.entry.StartActivity.sendReceive;


// Bluetooth Connection (between already paired devices)
public class BT_Activity extends AppCompatActivity  {
    private static final int CONT = 1;
    private static final int SILENT = 2 ;
    Button listen;
    ListView listView;
    Intent intent;
    public static TextView status;
    boolean conn;

    //BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;
    /////*****///////******
    //public static SendReceive sendReceive;

    ////*****///////*****
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    public static final int STATE_MESSAGE_RECEIVED = 5;
    private static final String TAG = "BT_Activity";
    private static final String APP_NAME = "BTchat";
    private static final UUID My_UUID = UUID.fromString("6001c680-7370-11ea-bc55-0242ac130003");
    public static SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt);
        findViewByIds();
        conn = false;
        implementListeners();
        listPairedDevices();
        if (savedInstanceState != null) {
            status.setText(savedInstanceState.getCharSequence("status"));
            conn = savedInstanceState.getBoolean("conn");
        }
        intent = new Intent(this, TorpedoActivity.class);
        // connect to shared preferences
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }
    
    // create BT paired devices' list
    private void  listPairedDevices(){
        Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
        String[] strings = new String[bt.size()];
        int index = 0;
        if (bt.size() > 0) {
            btArray = new BluetoothDevice[bt.size()];
            for (BluetoothDevice device : bt) {
                btArray[index] = device;
                strings[index] = device.getName();
                index++;
            }
            // list paired devices on screen
            ListItemAdapter arrayAdapter = new ListItemAdapter(getApplicationContext(), R.layout.listitem, strings);
            listView.setAdapter(arrayAdapter);
        }
    }

    // listeners for either Server or Client
    private void implementListeners() {
        // waits for incoming BT connection (becomes "server")
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerClass serverClass = new ServerClass();
                serverClass.start();
            }
        });

        // initiates BT Connection (becomes "client")
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //instantiate a new client with the selected BT partner
                ClientClass clientClass = new ClientClass(btArray[position]);
                clientClass.start();
                status.setText("Connecting");
            }
        });
    }

    // creates  menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // handles menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // starts SettingsActivity if "sound" has been selected from the menu
        if(id==R.id.sound){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    // handler for BT communication
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    conn = true;
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection failed");
                    break;
            }
            return true;
        }
    });
    
    // assign code to views
    private void findViewByIds() {
        listen = (Button) findViewById(R.id.listen);
        listView = (ListView) findViewById(R.id.listview);
        status = (TextView) findViewById(R.id.status);
    }

    // inner class Server
    private class ServerClass extends Thread {
        private BluetoothServerSocket serverSocket;

        // constructor
        public ServerClass() {
            try {
                serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, My_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            BluetoothSocket socket = null;
            while (socket == null) {
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = serverSocket.accept();//upon connection to a client the accept method returns the socket, which will be given to the variable socket
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }
                if (socket != null) {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);
                    sendReceive = new SendReceive(socket);
                    sendReceive.start();
                    startActivityForResult(intent, CONT); // upon successfull connection to a BT client starts TorpedoActivity
                }
            }
        }
    }

    // inner class Client
    private class ClientClass extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        // constructor
        public ClientClass(BluetoothDevice device1) {
            device = device1;
            try {
                socket = device.createRfcommSocketToServiceRecord(My_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
                Log.d(TAG, "INTENT");
                //startActivity(intent);
                startActivityForResult(intent, CONT);

            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    // save activity
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("status", status.getText());
        outState.putBoolean("conn", conn);
    }

    // continue activity upon completion of other activities
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

