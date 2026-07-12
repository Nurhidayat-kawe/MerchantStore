package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class StatusServer {
    @SerializedName("id_status")
    private String id_status;
    @SerializedName("nama_server")
    private String nama_server;
    @SerializedName("cabang")
    private int cabang;
    @SerializedName("tgl_ed")
    private String tgl_ed;
    @SerializedName("tgl_bayar")
    private String tgl_bayar;
    @SerializedName("tenggang_point")
    private String tenggang_point;

    public String getId_status() { return id_status; }
    public String getNama_server() { return nama_server; }
    public int getCabang() { return cabang; }
    public String getTgl_ed() { return tgl_ed; }
    public String getTgl_bayar() { return tgl_bayar; }
    public String getTenggang_point() { return tenggang_point; }
}
