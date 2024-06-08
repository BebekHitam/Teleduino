package com.example.radar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.teleduino.R;
import com.example.telemetri.BluetoothService;

import java.util.ArrayList;

public class bluetoothScan extends AppCompatActivity implements BluetoothService.DataReceivedListener {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 3;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService bluetoothService;

    private ArrayAdapter<String> deviceListAdapter;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();


    private TextView BluetoothAddress;
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

        Button scanButton = findViewById(R.id.scanButton);
        ListView deviceListView = findViewById(R.id.deviceListView);
        BluetoothAddress = findViewById(R.id.selected_device_data);

        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(deviceListAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (bluetoothAdapter == null) {
            Toast.makeText(this, "gaada device", Toast.LENGTH_SHORT).show();
            return;

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

        requestPermissions();

    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

//    private void startScanning(){
//
//        getPackageManager();
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//        } else {
//            bluetoothAdapter.startDiscovery();
//        }
//    }

    private void startScanning() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothAdapter.startDiscovery();
            Toast.makeText(this, "Scanning for devices...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startScanning();
            } else {
                Toast.makeText(this, "Location permission is required for Bluetooth scanning", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void connectToDevice(BluetoothDevice device){
        if (bluetoothService != null) {
            bluetoothService.close();
        }
//        theAddress ="";
//        //bluetoothService = new BluetoothService(device.getAddress(), this);
//        theAddress = String.valueOf(new BluetoothService(device.getAddress(), this));
//        BluetoothAdress.setText(theAddress);
        theAddress = device.getAddress();
        //bluetoothService = new BluetoothService(theAddress, (View.OnClickListener) this);
        BluetoothAddress.setText(theAddress);


    }
    private void sendAddressToFragment(String address) {
        Bundle bundle = new Bundle();
        bundle.putString("BLUETOOTH_ADDRESS", address);

//        YourFragment fragment = new YourFragment();
//        fragment.setArguments(bundle);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .addToBackStack(null)
//                .commit();
    }

    @Override
    public void onDataReceived(String data) {

    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
            }, REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_LOCATION_PERMISSION);
        }
    }
}
