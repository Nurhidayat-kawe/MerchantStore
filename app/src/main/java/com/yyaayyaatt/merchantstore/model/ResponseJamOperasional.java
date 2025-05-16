package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseJamOperasional {
    @SerializedName("result")
    List<JamOperasional> result;
    @SerializedName("kode")
    private String mKode;
    @SerializedName("pesan")
    private String mPesan;

    public List<JamOperasional> getResult() {
        return result;
    }

    public void setResult(List<JamOperasional> result) {
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
