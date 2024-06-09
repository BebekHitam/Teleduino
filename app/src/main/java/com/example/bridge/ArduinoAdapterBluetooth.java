package com.example.bridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArduinoAdapterBluetooth extends RecyclerView.Adapter<ArduinoAdapterBluetooth.DataViewHolder> {
    private static List<String> dataIn;

    public ArduinoAdapterBluetooth(List<String> dataIn){
        this.dataIn = dataIn;
    }

    public static void updateStreamData(String newStreamIn) {
        dataIn.add(newStreamIn);
        //notifyDataSetChanged();
    }

    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new DataViewHolder(view);

    }



    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.textView.setText(dataIn.get(position));

    }

    @Override
    public int getItemCount() {
        return dataIn.size();
    }

    public void addData(String data) {
        dataIn.add(data);
        notifyItemInserted(dataIn.size() - 1);
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        DataViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
