package com.example.bt_torpedo.entry;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt_torpedo.R;
import com.example.bt_torpedo.communication.SendReceive;


// Start Screen with options to start either Torpedo either over Bluetooth or Network

public class StartActivity extends AppCompatActivity {
    Button btBT;
    Button btNET;
    Intent intentBT, intentNET;
    public static BluetoothAdapter bluetoothAdapter;
    public static WifiManager wifiManager;
    int REQUEST_ENABLE_BLUETOOTH = 1;
    public static SendReceive sendReceive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page_new);
        btBT = findViewById(R.id.Bt);
        btNET = findViewById(R.id.Net);
        intentBT = new Intent(this, BT_Activity.class); // BT intent
        intentNET = new Intent(this, NET_Activity.class); // NET intent

        btBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
                    do {
                    } while (!bluetoothAdapter.isEnabled());
                }
                startActivity(intentBT); //start BT
            }
        });

        btNET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                if (!wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(true);
                }
                startActivity(intentNET); //Start NET
            }
        });
    }


}
