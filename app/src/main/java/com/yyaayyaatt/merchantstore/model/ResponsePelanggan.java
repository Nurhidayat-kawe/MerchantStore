package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePelanggan {
    @SerializedName("result")
    List<Pelanggan> result;
    @SerializedName("kode")
    private String mKode;
    @SerializedName("pesan")
    private String mPesan;

    public List<Pelanggan> getResult() {
        return result;
    }

    public void setResult(List<Pelanggan> result) {
        this.result = result;
    }

    public String getmKode() {
        return mKode;
    }

    public void setmKode(String mKode) {
        this.mKode = mKode;
    }

    public String getmPesan() {
        return mPesan;
    }

    public void setmPesan(String mPesan) {
        this.mPesan = mPesan;
    }
}
