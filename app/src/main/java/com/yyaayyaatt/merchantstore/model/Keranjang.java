package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class Keranjang {
    @SerializedName("id_keranjang")
    private int id_keranjang;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("id_produk")
    private String id_produk;
    @SerializedName("jml")
    private String jml;

    public int getId_keranjang() {
        return id_keranjang;
    }

    public void setId_keranjang(int id_keranjang) {
        this.id_keranjang = id_keranjang;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getJml() {
        return jml;
    }

    public void setJml(String jml) {
        this.jml = jml;
    }
}
