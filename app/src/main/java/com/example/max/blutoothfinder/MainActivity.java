package com.example.max.blutoothfinder;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    TextView statusTextView;
    Button searchButton;
    BluetoothAdapter mBluetoothAdapter;
    ArrayList<String> blutoothDevices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ArrayAdapter mArrayAdapter;

    private final BroadcastReseiver mBroadcastReseiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            Log.i("Acton", action);
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                statusTextView.setText("Finished...");
                searchButton.setEnabled(true);

            }else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                int rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
               // Log.i("Device Found", "Name"  + name + "Address: " + address + "RSSI" + rssi  );
                if (!addresses.contains(address)){
                    addresses.add(address);
                    addresses.add(address);
                    String deviceString = "";
                    if (name == null || name.equals("")){
                        deviceString = address + " " + "-RSSI" + rssi + "dBm";

                    }else {
                        deviceString = name + " " + "-RSSI" + rssi + "dBm";
                    }if (!blutoothDevices.contains(deviceString)){
                        blutoothDevices.add(deviceString);
                        mArrayAdapter.notifyDataSetChanged();
                    }
                }




            }
        }
    };

    public  void searchClicked (View view){
        statusTextView.setText("Searching...");
        searchButton.setEnabled(false);
        blutoothDevices.clear();
        addresses.clear();
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.listView);
        statusTextView = findViewById(R.id.statusTextView);
        searchButton = findViewById(R.id.searchButton);
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, blutoothDevices);

        mListView.setAdapter(mArrayAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_FOUND);
        registerReceiver(mBroadcastReseiver, intentFilter);
        mBluetoothAdapter.startDiscovery();
    }
}
