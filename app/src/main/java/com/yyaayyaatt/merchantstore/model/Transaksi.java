package com.yyaayyaatt.merchantstore.model;

import com.google.gson.annotations.SerializedName;

public class Transaksi {
    @SerializedName("id")
    private int id;
    @SerializedName("id_transaksi")
    private String id_transaksi;
    @SerializedName("id_user")
    private int id_user;
    @SerializedName("nama")
    private String nama;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("jam")
    private String jam;
    @SerializedName("status")
    private String status;
    @SerializedName("diskon")
    private String diskon;
    @SerializedName("ongkir")
    private String ongkir;
    @SerializedName("status_bayar")
    private String status_bayar;
    @SerializedName("foto_user")
    private String foto_user;
    @SerializedName("toko")
    private String toko;

    @SerializedName("trans_point")
    private String point;


    @SerializedName("id_details")
    private int id_details;
    @SerializedName("produk")
    private String produk;
    @SerializedName("jumlah")
    private String jumlah;
    @SerializedName("harga")
    private String harga;
    @SerializedName("stok")
    private String stok;
    @SerializedName("untung")
    private String untung;

    @SerializedName("id_produk")
    private String id_produk;
    @SerializedName("jml_produk")
    private String jml_produk;
    @SerializedName("jml_trans")
    private String jml_trans;
    @SerializedName("total")
    private String total;

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
    @SerializedName("id_satuan")
    private int id_satuan;
    @SerializedName("nama_satuan")
    private String nama_satuan;
    @SerializedName("deskripsi")
    private String deskripsi;
    @SerializedName("foto")
    private String foto;
    //cabang
    @SerializedName("id_cabang")
    private String id_cabang;
    @SerializedName("nama_cabang")
    private String nama_cabang;
    @SerializedName("alamat_cabang")
    private String alamat_cabang;
    @SerializedName("telp_cabang")
    private String telp_cabang;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public int getId_details() {
        return id_details;
    }

    public void setId_details(int id_details) {
        this.id_details = id_details;
    }

    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJml_trans() {
        return jml_trans;
    }

    public void setJml_trans(String jml_trans) {
        this.jml_trans = jml_trans;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getStatus_bayar() {
        return status_bayar;
    }

    public void setStatus_bayar(String status_bayar) {
        this.status_bayar = status_bayar;
    }

    public String getFoto_user() {
        return foto_user;
    }

    public void setFoto_user(String foto_user) {
        this.foto_user = foto_user;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getJml_produk() {
        return jml_produk;
    }

    public void setJml_produk(String jml_produk) {
        this.jml_produk = jml_produk;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getId_cabang() {
        return id_cabang;
    }

    public void setId_cabang(String id_cabang) {
        this.id_cabang = id_cabang;
    }

    public String getNama_cabang() {
        return nama_cabang;
    }

    public void setNama_cabang(String nama_cabang) {
        this.nama_cabang = nama_cabang;
    }

    public String getAlamat_cabang() {
        return alamat_cabang;
    }

    public void setAlamat_cabang(String alamat_cabang) {
        this.alamat_cabang = alamat_cabang;
    }

    public String getTelp_cabang() {
        return telp_cabang;
    }

    public void setTelp_cabang(String telp_cabang) {
        this.telp_cabang = telp_cabang;
    }

    public String getUntung() {
        return untung;
    }

    public void setUntung(String untung) {
        this.untung = untung;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
