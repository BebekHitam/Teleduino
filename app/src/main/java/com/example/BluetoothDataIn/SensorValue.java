package com.example.BluetoothDataIn;

public class SensorValue {
    String theValue;

//    public BluetoothDataInholder(String value){
//        this.SensorValue = value;
//    }

    public SensorValue(String sensorValue){
        this.theValue = sensorValue;
    }

    public String getSensorValue(){
        return theValue;
    }
}
