package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class Referral {
    @SerializedName("id_user")
    private int id_user;
    @SerializedName("nama")
    private String nama_referrer;
    @SerializedName("id_referree")
    private int id_referree;
    @SerializedName("nama_referree")
    private String nama_referree;
    @SerializedName("jml_referree")
    private String jml_referree;
    @SerializedName("foto_user")
    private String foto;
    @SerializedName("tgl_daftar")
    private String tgl_daftar;
    @SerializedName("foto_referree")
    private String foto_referre;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNama_referrer() {
        return nama_referrer;
    }

    public void setNama_referrer(String nama_referrer) {
        this.nama_referrer = nama_referrer;
    }

    public String getNama_referree() {
        return nama_referree;
    }

    public void setNama_referree(String nama_referree) {
        this.nama_referree = nama_referree;
    }

    public String getJml_referree() {
        return jml_referree;
    }

    public void setJml_referree(String jml_referree) {
        this.jml_referree = jml_referree;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId_referree() {
        return id_referree;
    }

    public void setId_referree(int id_referree) {
        this.id_referree = id_referree;
    }

    public String getFoto_referre() {
        return foto_referre;
    }

    public void setFoto_referre(String foto_referre) {
        this.foto_referre = foto_referre;
    }

    public String getTgl_daftar() {
        return tgl_daftar;
    }

    public void setTgl_daftar(String tgl_daftar) {
        this.tgl_daftar = tgl_daftar;
    }
}
