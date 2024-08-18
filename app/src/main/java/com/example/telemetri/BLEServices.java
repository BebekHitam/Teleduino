package com.example.telemetri;

import static android.system.Os.close;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.UUID;

public class BLEServices {
    private static final String key = "BluetoothService";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothService.DataReceivedListener dataReceivedListener;

    private static final UUID SERVICE_UUID = UUID.fromString("SERVICE_UUID_HERE");
    private static final UUID CHARACTERISTIC_UUID = UUID.fromString("CHARACTERISTIC_UUID_HERE");

    public interface DataReceivedListener {
        void onDataReceived(String Data);
    }

    public BLEServices(Context context, String deviceAddress, DataReceivedListener listener) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        dataReceivedListener = listener;

        bluetoothGatt = device.connectGatt(context, false, gattCallback);
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.d(key, "Connected to device");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                Log.d(key, "Disconnected from Device");
                close();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS){
                BluetoothGattService service = gatt.getService(SERVICE_UUID);
                if (service != null){
                    characteristic = service.getCharacteristic(CHARACTERISTIC_UUID);
                    gatt.setCharacteristicNotification(characteristic, true);
                }else {
                    Log.e(TAG, "Service not Found");
                }
            } else {
                Log.e(TAG, "Service discovery failed with Status: " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic) {
            final String data = characteristic.getStringValue(0);
            if (dataReceivedListener != null) {
                dataReceivedListener.onDataReceived(data);
            }
        }
    };
    public void close(){
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }
}
