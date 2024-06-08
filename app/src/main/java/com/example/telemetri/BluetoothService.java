package com.example.telemetri;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothService {
    private static final String TAG = "Bluetooth Services";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private DataReceivedListener dataReceivedListener;

    public interface DataReceivedListener{
        void onDataReceived(String data);
    }

    public BluetoothService(String deviceAddress, View.OnClickListener bluetoothScan){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
        //dataReceivedListener = listener;

        try{
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            inputStream = bluetoothSocket.getInputStream();
        } catch (IOException e){
            Log.e(TAG, "Tidak bisa berkomunikasi antar bluetooth", e);
        }

    }

    public String readData(){
        byte[] buffer = new byte[1024]; //arduino cuma 10 bit ya
        int bytes;

        try {
            bytes = inputStream.read(buffer);
            return new String(buffer, 0, bytes);
        } catch (IOException e) {
            Log.e(TAG, "gak bisa membaca data", e);
            return null;
        }
    }
    public void close(){
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        }catch (IOException e){
            Log.e(TAG, "Error closing bluetooth socket", e);
        }
    }





}
