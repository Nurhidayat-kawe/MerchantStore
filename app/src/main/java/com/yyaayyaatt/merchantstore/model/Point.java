package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class Point {
    @SerializedName("id")
    private String id;
    @SerializedName("nominal")
    private String nominal;
    @SerializedName("periode")
    private String periode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }
}
