package com.example.radar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.teleduino.R;
import com.example.telemetri.BluetoothService;

import java.util.ArrayList;

public class bluetoothScan extends AppCompatActivity implements BluetoothService.DataReceivedListener {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService bluetoothService;

    private ArrayAdapter<String> deviceListAdapter;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();


    private TextView textView;
    public String theAddress;



    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device != null && device.getName() != null) {
                    deviceList.add(device);
                    deviceListAdapter.add(device.getName() + "\n" + device.getAddress());
                    deviceListAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_scan_screen);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button scanButton = findViewById(R.id.scan_the_bluetooth);
        ListView deviceListView = findViewById(R.id.deviceListView);
        textView = findViewById(R.id.selected_device_data);

        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(deviceListAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "gaada device", Toast.LENGTH_SHORT).show();

            //return view;
        }
        if (!bluetoothAdapter.isEnabled()){
            Intent enableBluetoothPlease = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothPlease, REQUEST_ENABLE_BT);
        }

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
            }
        });
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = deviceList.get(position);
                connectToDevice(device);
            }
        });

    }
    private void startScanning(){

        getPackageManager();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startScanning();
            }
        }
    }
    private void connectToDevice(BluetoothDevice device){
        if (bluetoothService != null) {
            bluetoothService.close();
        }
        theAddress ="";
        //bluetoothService = new BluetoothService(device.getAddress(), this);
        theAddress = String.valueOf(new BluetoothService(device.getAddress(), this));
        Bundle bundle = new Bundle();
        bundle.putString();

    }

    @Override
    public void onDataReceived(String data) {

    }
}
