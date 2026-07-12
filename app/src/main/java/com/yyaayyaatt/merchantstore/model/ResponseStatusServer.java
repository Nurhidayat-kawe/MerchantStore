package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseStatusServer {
    @SerializedName("result")
    private List<StatusServer> result;
    @SerializedName("kode")
    private String mKode;
    @SerializedName("pesan")
    private String mPesan;

    public List<StatusServer> getResult() { return result; }
    public String getmKode() { return mKode; }
    public String getmPesan() { return mPesan; }
}
