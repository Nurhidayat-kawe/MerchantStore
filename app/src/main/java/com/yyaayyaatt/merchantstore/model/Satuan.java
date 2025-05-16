package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class Satuan {
    @SerializedName("id_satuan")
    private int id_satuan;
    @SerializedName("nama_satuan")
    private String nama_satuan;

    public int getId_satuan() {
        return id_satuan;
    }

    public void setId_satuan(int id_satuan) {
        this.id_satuan = id_satuan;
    }

    public String getNama_satuan() {
        return nama_satuan;
    }

    public void setNama_satuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }
}
