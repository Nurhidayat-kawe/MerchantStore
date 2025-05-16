package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class Settings {
    @SerializedName("id")
    private int id;
    @SerializedName("status_toko")
    private String status_toko;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus_toko() {
        return status_toko;
    }

    public void setStatus_toko(String status_toko) {
        this.status_toko = status_toko;
    }
}
