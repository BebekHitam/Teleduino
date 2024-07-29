package com.example.BluetoothDataIn;

public class DhtEleven {
    private String suhu;
    private String Kelembaban;

    public DhtEleven(String suhu, String kelembaban){
        this.suhu = suhu;
        this.Kelembaban = kelembaban;
    }

    public String getSuhu() {
        return suhu;
    }
    public void setSuhu(String suhu) {
        this.suhu = suhu;
    }

    public String getKelembaban() {
        return Kelembaban;
    }
    public void setKelembaban(String kelembaban) {
        this.Kelembaban = kelembaban;
    }



}
