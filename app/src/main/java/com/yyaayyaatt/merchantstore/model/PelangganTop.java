package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class PelangganTop {
    @SerializedName("id_user")
    private int id_user;
    @SerializedName("nama")
    private String nama;
    @SerializedName("foto_user")
    private String foto;
    @SerializedName("toko")
    private String toko;

    @SerializedName("jml_trans")
    private String jml_trans;
    @SerializedName("jumlah")
    private String jumlah;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("telp")
    private String telp;
    @SerializedName("stat_user")
    private String status;
    @SerializedName("tgl_daftar")
    private String tgl_daftar;
    @SerializedName("point")
    private String point;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getJml_trans() {
        return jml_trans;
    }

    public void setJml_trans(String jml_trans) {
        this.jml_trans = jml_trans;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTgl_daftar() {
        return tgl_daftar;
    }

    public void setTgl_daftar(String tgl_daftar) {
        this.tgl_daftar = tgl_daftar;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
