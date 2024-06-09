package com.example.bridge;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BluetoothDataIn.SensorValue;

import java.util.List;

public class ArduinoAdapterBluetooth extends RecyclerView.Adapter<ArduinoAdapterBluetooth> {
    private static List<SensorValue> dataIn;

    public static void updateStreamData(SensorValue newStreamIn) {
        dataIn.add(newStreamIn);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ArduinoAdapterBluetooth onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ArduinoAdapterBluetooth holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
