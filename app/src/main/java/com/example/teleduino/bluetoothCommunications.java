package com.example.teleduino;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BluetoothDataIn.SensorValue;
import com.example.bridge.ArduinoAdapterBluetooth;
import com.example.radar.bluetoothScan;
import com.example.telemetri.BluetoothService;


public class bluetoothCommunications extends Fragment {
    private Toolbar toolbar;
    private static final String TAG = "frame bluetooth";
    private static String DEVICE_ADDRESS = "0:0";

    private TextView dataIn;
    private EditText addressBluetooth;
    private BluetoothService bluetoothService;
    private Button oke, scanNearbyDevice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_communications, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addressBluetooth = view.findViewById(R.id.for_address);
        addressBluetooth.setHint(DEVICE_ADDRESS);
        dataIn = view.findViewById(R.id.datanya);

        //Disini tolong lakukan scan
        scanNearbyDevice = view.findViewById(R.id.scan_the_bluetooth);
        scanNearbyDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pindah ke jendela mencari device
                Intent intent = new Intent(getActivity(), bluetoothScan.class);
                startActivity(intent);
            }
        });
        //taruh disini DEVICE_ADDRESSNYA, setelah scan untuk mengubah value "0:0"


        oke = view.findViewById(R.id.to_launch);


        oke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addressCheck = DEVICE_ADDRESS.length();
                if (addressCheck >= 4){
                    DEVICE_ADDRESS = String.valueOf(addressBluetooth.getText()); // ini tunggu set on click dulu
                    bluetoothService = new BluetoothService(DEVICE_ADDRESS, this);
                    runTheConnection();
                } else {
                    Toast.makeText(getContext(), "masukkan address yang benar", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
    public void runTheConnection(){
        Thread readBluetoothData = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    final String receivedDataFromArduino = bluetoothService.readData();
                    if (receivedDataFromArduino != null){
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                dataIn.setText(receivedDataFromArduino);
                            }
                        });
                    }
                }
            }
        });
        readBluetoothData.start();
    }

    public void rinTheConnectionTwo(){
        Thread readBluetoothin = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final String receivedDataFromArduinoTwo = bluetoothService.readData();
                    if (receivedDataFromArduinoTwo != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //perse dulu datanya
                                String[] streamInputData = receivedDataFromArduinoTwo.split(",");
                                double rawSensorValue = Double.parseDouble(streamInputData[0]);

                                //initiate Sensor data object
                                SensorValue sensorValue = new SensorValue(String.valueOf(rawSensorValue));

                                //update adapter list
                                ArduinoAdapterBluetooth.updateStreamData(sensorValue);


                            }
                        });
                    }
                }
            }
        })
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if (bluetoothService != null) {
            bluetoothService.close();
        }


    }
}