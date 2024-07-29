package com.example.bridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BluetoothDataIn.DhtEleven;
import com.example.teleduino.R;

import java.util.List;

public class ArduinoAdapterBluetooth extends RecyclerView.Adapter<ArduinoAdapterBluetooth.DataViewHolder> {
    private static List<DhtEleven> dataIn;
    private LayoutInflater mlayoutInflater;

    public ArduinoAdapterBluetooth(List<DhtEleven> dataIn){
        //this.mlayoutInflater = LayoutInflater.from(context);
        this.dataIn = dataIn;
    }

//    public static void updateStreamData(String newStreamIn) {
//        dataIn.add(newStreamIn);
//        //notifyDataSetChanged();
//    }

    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mlayoutInflater.inflate(R.layout.item_list_layout, parent, false);
        return new DataViewHolder(view);

    }



    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        //holder.textView.setText(dataIn.get(position));
        DhtEleven item = dataIn.get(position);
        holder.suhu.setText(item.getSuhu());
        holder.kelembaban.setText(item.getKelembaban());
    }

    @Override
    public int getItemCount() {
        return dataIn.size();
    }

    public void addData(String suhu, String kelembaban) {
        dataIn.add(new DhtEleven(suhu, kelembaban));
        notifyItemInserted(dataIn.size() - 1);
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        //TextView textView;
        TextView suhu, kelembaban;

        DataViewHolder(View itemView) {
            super(itemView);
            //textView = itemView.findViewById(android.R.id.text1);
            suhu = itemView.findViewById(R.id.suhu);
            kelembaban = itemView.findViewById(R.id.kelembaban);
        }
    }
}
