package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("id_user")
    private int id_user;
    @SerializedName("nama")
    private String nama;
    @SerializedName("telp")
    private String telp;
    @SerializedName("tgl_daftar")
    private String tgl_daftar;
    @SerializedName("foto_user")
    private String foto_user;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("toko")
    private String toko;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("cabang")
    private String cabang;
    @SerializedName("jml_user")
    private String jml_user;

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

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getTgl_daftar() {
        return tgl_daftar;
    }

    public void setTgl_daftar(String tgl_daftar) {
        this.tgl_daftar = tgl_daftar;
    }

    public String getFoto() {
        return foto_user;
    }

    public void setFoto(String foto_user) {
        this.foto_user = foto_user;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }

    public String getJml_user() {
        return jml_user;
    }

    public void setJml_user(String jml_user) {
        this.jml_user = jml_user;
    }
}
