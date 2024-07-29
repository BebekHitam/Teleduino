package com.example.teleduino;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BluetoothDataIn.DhtEleven;
import com.example.bridge.ArduinoAdapterBluetooth;
import com.example.radar.bluetoothScan;
import com.example.telemetri.BluetoothService;

import java.util.List;


public class BluetoothCommunications extends Fragment {
    private Toolbar toolbar;
    private static final String TAG = "frame bluetooth";
    private static String DEVICE_ADDRESS = "0:0";


    private EditText addressBluetooth;
    private BluetoothService bluetoothService;
    private Button oke, scanNearbyDevice;
    private ArduinoAdapterBluetooth arduinoAdapter;
    private TextView dataIn;
    private List<DhtEleven> data;
    private RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null){
            String message = bundle.getString("device_address");
            addressBluetooth.setText(message);

        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_communications, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addressBluetooth = view.findViewById(R.id.for_address);
        addressBluetooth.setHint(DEVICE_ADDRESS);
        dataIn = view.findViewById(R.id.datanya);
        recyclerView = view.findViewById(R.id.data_stream_in);

        arduinoAdapter = new ArduinoAdapterBluetooth(data);
        recyclerView.setAdapter(arduinoAdapter);

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
                                String[] data = receivedDataFromArduino.split(",");
                                if (data.length == 2) {
                                    String suhu = data[0].trim();
                                    String kelembaban = data[1].trim();
                                    arduinoAdapter.addData(suhu, kelembaban);
                                }

                            }
                        });
                    }
                }
            }
        });
        readBluetoothData.start();
    }

//    public void rinTheConnectionTwo(){
//        Thread readBluetoothin = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    final String receivedDataFromArduinoTwo = bluetoothService.readData();
//                    if (receivedDataFromArduinoTwo != null) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //perse dulu datanya
//                                String[] streamInputData = receivedDataFromArduinoTwo.split(",");
//                                double rawSensorValue = Double.parseDouble(streamInputData[0]);
//
//                                //initiate Sensor data object
//                                SensorValue sensorValue = new SensorValue(String.valueOf(rawSensorValue));
//
//                                //update adapter list
//                                ArduinoAdapterBluetooth.updateStreamData(sensorValue);
//
//
//                            }
//                        });
//                    }
//                }
//            }
//        });
//    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if (bluetoothService != null) {
            bluetoothService.close();
        }


    }
}