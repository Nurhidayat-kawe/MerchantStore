package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;
public class JamOperasional {
    @SerializedName("id_jam")
    private int id_jam;
    @SerializedName("hari")
    private String hari;
    @SerializedName("jam_buka")
    private String jam_buka;
    @SerializedName("jam_tutup")
    private String jam_tutup;

    public int getId_jam() {
        return id_jam;
    }

    public void setId_jam(int id_jam) {
        this.id_jam = id_jam;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getJam_buka() {
        return jam_buka;
    }

    public void setJam_buka(String jam_buka) {
        this.jam_buka = jam_buka;
    }

    public String getJam_tutup() {
        return jam_tutup;
    }

    public void setJam_tutup(String jam_tutup) {
        this.jam_tutup = jam_tutup;
    }
}
