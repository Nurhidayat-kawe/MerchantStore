package com.yyaayyaatt.merchantstore.service;

import com.yyaayyaatt.merchantstore.model.ResponseJamOperasional;
import com.yyaayyaatt.merchantstore.model.ResponseKategori;
import com.yyaayyaatt.merchantstore.model.ResponseKeranjang;
import com.yyaayyaatt.merchantstore.model.ResponsePelanggan;
import com.yyaayyaatt.merchantstore.model.ResponsePelangganTop;
import com.yyaayyaatt.merchantstore.model.ResponsePoint;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.model.ResponseReferral;
import com.yyaayyaatt.merchantstore.model.ResponseSatuan;
import com.yyaayyaatt.merchantstore.model.ResponseSettings;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BaseApiService {
    //    @FormUrlEncoded
    @GET("getPelangganCount.php")
    Call<ResponseUsers> getPelanggan();

    @GET("getStatusToko.php")
    Call<ResponseSettings> getStatusToko();

    @GET("getJamOperasional.php")
    Call<ResponseJamOperasional> getJamOperasional();

    @GET("getPelangganAll.php")
    Call<ResponsePelangganTop> getPelangganAll();

    @GET("getPelanggans.php")
    Call<ResponsePelanggan> getPelanggans();

    //
    @GET("getProdukCount.php")
    Call<ResponseProduk> getProdukCount();

    //
    @GET("getTransCount.php")
    Call<ResponseTransaksi> getTransCount();

    @GET("getKat.php")
    Call<ResponseKategori> getKat();

    @GET("getSatuan.php")
    Call<ResponseSatuan> getSatuan();

    @GET("getPelangganBaru.php")
    Call<ResponsePelanggan> getPelangganBaru();

    @GET("getPelangganMenunggu.php")
    Call<ResponsePelanggan> getPelangganMenunggu();

    @GET("getTransCountOrder.php")
    Call<ResponseTransaksi> getTransNotif();

    //
    @GET("getPendapatan.php")
    Call<ResponseTransaksi> getPendapatan();

    @GET("getPendapatanHarian.php")
    Call<ResponseTransaksi> getPendapatanHarian();

    @GET("getPendapatanHarianRetails.php")
    Call<ResponseTransaksi> getPendapatanHarianRetail();

    @GET("getPelangganTop.php")
    Call<ResponsePelangganTop> getPelangganTop();

    @GET("getTransaksiByStatus.php")
    Call<ResponseTransaksi> getPesanan();

    @GET("getProdukTop.php")
    Call<ResponseTransaksi> getProdukTop();

    @GET("getPoints.php")
    Call<ResponsePelanggan> getPointMembers();

    @GET("getReferrals.php")
    Call<ResponseReferral> getReferrals();
    @GET("cekBonusPoint.php")
    Call<ResponsePoint> cekBonusPoint();

    @FormUrlEncoded
    @POST("hapusProduk.php")
    Call<ResponseProduk> hapusProduk(@Field("id_produk") String id_produk);

    @FormUrlEncoded
    @POST("getReferralById.php")
    Call<ResponseReferral> getReferralById(@Field("id_user") int id_user);
    
    @FormUrlEncoded
    @POST("getTransaksiByStatus.php")
    Call<ResponseTransaksi> getPesanan(@Field("status") String key);

    @FormUrlEncoded
    @POST("getPendapatanByDate.php")
    Call<ResponseTransaksi> getLapTransaksi(@Field("tgl_awal") String tgl_awal,
                                            @Field("tgl_ahir") String tgl_ahir);
    @FormUrlEncoded
    @POST("updateStatusToko.php")
    Call<ResponseSettings> updateStatusToko(@Field("status_toko") String status_toko);
    @FormUrlEncoded
    @POST("updateJamOperasional.php")
    Call<ResponseJamOperasional> updateJamOperasional(@Field("jam_buka") String jam_buka,
                                                 @Field("jam_tutup") String jam_tutup,
                                                 @Field("hari") String hari);

    @FormUrlEncoded
    @POST("getRekapTransaksi.php")
    Call<ResponseTransaksi> getRekapTransaksi(@Field("tgl_awal") String tgl_awal,
                                              @Field("tgl_ahir") String tgl_ahir);
    @FormUrlEncoded
    @POST("getRekapTransaksiPoint.php")
    Call<ResponseTransaksi> getRekapTransaksiPoint(@Field("tgl_awal") String tgl_awal,
                                              @Field("tgl_ahir") String tgl_ahir);
    @FormUrlEncoded
    @POST("getPendapatanPertanggal.php")
    Call<ResponseTransaksi> getRekapPendapatan(@Field("tgl_awal") String tgl_awal,
                                               @Field("tgl_ahir") String tgl_ahir);

    @FormUrlEncoded
    @POST("getPelangganByKey.php")
    Call<ResponsePelangganTop> getPelangganBy(@Field("key") String key);

    @FormUrlEncoded
    @POST("getTransById.php")
    Call<ResponseTransaksi> getPesananById(@Field("id_trans") String id_trans);

    @FormUrlEncoded
    @POST("getNotaTrans.php")
    Call<ResponseTransaksi> getNotaTrans(@Field("id_trans") String id_trans);

    @FormUrlEncoded
    @POST("updateStatusTrans.php")
    Call<ResponseTransaksi> updateStatusTrans(@Field("id") int id, @Field("status") String status);
    @FormUrlEncoded
    @POST("updatePointPembelian.php")
    Call<ResponseUsers> updatePointUser(@Field("id_user") String id_user,
                                         @Field("jml_bonus_point") String jml_bonus_point);
    @FormUrlEncoded
    @POST("updateStatusBayar.php")
    Call<ResponseTransaksi> updateStatusBayar(@Field("id") int id, @Field("status") String status);

    @FormUrlEncoded
    @POST("updateStatusUser.php")
    Call<ResponseUsers> updateStatusUser(@Field("id_user") int id_user, @Field("stat_user") String stat_user);

    @FormUrlEncoded
    @POST("updateFotoUser.php")
    Call<ResponseUsers> updateFoto(@Field("id_user") int id_user,
                                   @Field("foto_user") String foto_user);

    @FormUrlEncoded
    @POST("getProdukById.php")
    Call<ResponseProduk> getProduk(@Field("id") String id);
    @FormUrlEncoded
    @POST("addKeranjangRetail.php")
    Call<ResponseKeranjang> addKeranjang(@Field("id_user") String id_user, @Field("id_produk") String id_produk, @Field("aksi") String aksi);
    @FormUrlEncoded
    @POST("getKeranjangRetail.php")
    Call<ResponseKeranjang> getKeranjang(@Field("id_user") String id_user,@Field("id_produk") String id_produk);

    @FormUrlEncoded
    @POST("getKeranjangById.php")
    Call<ResponseKeranjang> getKeranjangById(@Field("id_user") String id_user);
    @FormUrlEncoded
    @POST("updateFotoProduk.php")
    Call<ResponseProduk> updateFotoProduk(@Field("id_produk") String id_produk, @Field("foto") String foto);
    @FormUrlEncoded
    @POST("simpanPesananKeranjangRetail.php")
    Call<ResponseTransaksi> simpanPesananKeranjang(@Field("id_transaksi") String id_trans,
                                                   @Field("id_keranjang") int id_keranjang,
                                                   @Field("id_user") String id_user,
                                                   @Field("produk") String produk,
                                                   @Field("jumlah") String jumlah,
                                                   @Field("harga") String harga,
                                                   @Field("diskon") String diskon,
                                                   @Field("h_beli") String h_beli);
    @FormUrlEncoded
    @POST("getProdukAll.php")
    Call<ResponseProduk> getProduks(@Field("key") String key, @Field("kat") String kat);
    @FormUrlEncoded
    @POST("getProdukAllRetail.php")
    Call<ResponseProduk> getProduksRetail(@Field("key") String key, @Field("kat") String kat);
    @FormUrlEncoded
    @POST("cekStok.php")
    Call<ResponseProduk> cekStokProduk(@Field("id_produk") String id_produk);

    @FormUrlEncoded
    @POST("opname.php")
    Call<ResponseProduk> opname(@Field("id_produk") String id_produk, @Field("stok_baru") int stok_baru);

    @FormUrlEncoded
    @POST("getUserById.php")
    Call<ResponseUsers> getProfil(@Field("id_user") int id_user);

    @FormUrlEncoded
    @POST("cekTransPertama.php")
    Call<ResponseTransaksi> cekTransPertama(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseUsers> login(@Field("username") String username,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST("addProduk.php")
    Call<ResponseProduk> addProduk(@Field("id_produk") String id_produk,
                                   @Field("nama_produk") String nama_produk,
                                   @Field("id_kategori") String id_kategori,
                                   @Field("harga_beli") String harga_beli,
                                   @Field("id_satuan") String id_satuan,
                                   @Field("deskripsi") String deskripsi,
                                   @Field("foto") String foto,
                                   @Field("foto2") String foto2,
                                   @Field("user") String user,
                                   @Field("cabang") String cabang,
                                   @Field("stok") int stok,
                                   @Field("jml_beli") int jml_beli,
                                   @Field("harga_disc") String harga_disc,
                                   @Field("harga_jual") String harga_jual,
                                   @Field("jml_point") int jml_point);

    @FormUrlEncoded
    @POST("updateProduk.php")
    Call<ResponseProduk> updateProduk(@Field("id_produk") String id_produk,
                                      @Field("nama_produk") String nama_produk,
                                      @Field("id_kategori") String id_kategori,
                                      @Field("harga_beli") String harga_beli,
                                      @Field("id_satuan") String id_satuan,
                                      @Field("deskripsi") String deskripsi,
                                      @Field("user") String user,
                                      @Field("harga_jual") String harga_jual,
                                      @Field("jml_beli") String jml_beli,
                                      @Field("harga_diskon") String harga_diskon,
                                      @Field("jml_point") int jml_point);

    @FormUrlEncoded
    @POST("setDiskon.php")
    Call<ResponseProduk> setDiskon(@Field("id_produk") String id_produk,
                                   @Field("jml_beli") String jml_beli,
                                   @Field("harga_diskon") String harga_diskon);

    @FormUrlEncoded
    @POST("getTransaksiPointByStatusUser.php")
    Call<ResponseTransaksi> getPesananPoint(@Field("status") String key,
                                            @Field("id_user") String id_user);
    @FormUrlEncoded
    @POST("getTransPointById.php")
    Call<ResponseTransaksi> getPesananPointById(@Field("id_trans") String id_trans);
}