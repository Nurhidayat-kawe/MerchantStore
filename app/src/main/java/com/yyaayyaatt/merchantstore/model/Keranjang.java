package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class Keranjang {
    @SerializedName("id_produk")
    private String id_produk;
    @SerializedName("nama_produk")
    private String nama_produk;
    @SerializedName("id_kategori")
    private int id_kategori;
    @SerializedName("nama_kategori")
    private String nama_kategori;
    @SerializedName("harga_beli")
    private String harga_beli;
    @SerializedName("harga_jual")
    private String harga_jual;
    @SerializedName("harga_disc")
    private String harga_disc;
    @SerializedName("id_satuan")
    private int id_satuan;
    @SerializedName("nama_satuan")
    private String nama_satuan;
    @SerializedName("deskripsi")
    private String deskripsi;
    @SerializedName("foto")
    private String foto;
    @SerializedName("user")
    private int user;
    @SerializedName("nama")
    private String nama;
    @SerializedName("created")
    private String created;
    @SerializedName("updated")
    private String updated;
    @SerializedName("deleted")
    private String deleted;
    @SerializedName("barcode")
    private String barcode;
    @SerializedName("jml_produk")
    private String jml_produk;
    @SerializedName("stok")
    private int stok;
    @SerializedName("jml_beli")
    private int jml_beli;
    @SerializedName("jml_trans")
    private int jml_trans;
    //Keranjang
    @SerializedName("id")
    private int id;
    @SerializedName("id_keranjang")
    private int id_keranjang;
    @SerializedName("id_user")
    private int id_user;
    @SerializedName("jml")
    private int jml;

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getHarga_beli() {
        return harga_beli;
    }

    public void setHarga_beli(String harga_beli) {
        this.harga_beli = harga_beli;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }

    public int getId_satuan() {
        return id_satuan;
    }

    public void setId_satuan(int id_satuan) {
        this.id_satuan = id_satuan;
    }

    public String getNama_satuan() {
        return nama_satuan;
    }

    public void setNama_satuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getJml_produk() {
        return jml_produk;
    }

    public void setJml_produk(String jml_produk) {
        this.jml_produk = jml_produk;
    }

    public String getHarga_disc() {
        return harga_disc;
    }

    public void setHarga_disc(String harga_disc) {
        this.harga_disc = harga_disc;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getJml_beli() {
        return jml_beli;
    }

    public void setJml_beli(int jml_beli) {
        this.jml_beli = jml_beli;
    }

    public int getJml_trans() {
        return jml_trans;
    }

    public void setJml_trans(int jml_trans) {
        this.jml_trans = jml_trans;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_keranjang() {
        return id_keranjang;
    }

    public void setId_keranjang(int id_keranjang) {
        this.id_keranjang = id_keranjang;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getJml() {
        return jml;
    }

    public void setJml(int jml) {
        this.jml = jml;
    }
}
