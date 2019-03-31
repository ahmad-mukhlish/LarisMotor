package com.yayanheryanto.larismotor.retrofit;

import com.yayanheryanto.larismotor.model.Customer;
import com.yayanheryanto.larismotor.model.Detail;
import com.yayanheryanto.larismotor.model.Merk;
import com.yayanheryanto.larismotor.model.MerkTipe;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.model.PendingBeli;
import com.yayanheryanto.larismotor.model.PendingJual;
import com.yayanheryanto.larismotor.model.Sales;
import com.yayanheryanto.larismotor.model.Tipe;
import com.yayanheryanto.larismotor.model.Transaksi;
import com.yayanheryanto.larismotor.model.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("api/getMotor")
    Call<List<Motor>> getMotor(@Query("id_user") String id_user);

    @GET("api/getId")
    Call<List<User>> getId();

    @GET("api/getSales")
    Call<List<Sales>> getSales(@Header("Authorization") String authorization);

    @GET("api/getMerk")
    Call<List<Merk>> getMerk();

    @GET("api/getMerkById/{id_merk}/idTipe/{id_tipe}")
    Call<List<MerkTipe>> getMerkById(@Query("id_merk") String id_merk, @Query("id_tipe") String id_tipe);

    @GET("api/getTipe/{id_merk}")
    Call<List<Tipe>> getTipe(@Query("id_merk") String id_merk);

    @FormUrlEncoded
    @POST("api/login")
    Call<User> getUser(@Field("username") String username,
                       @Field("password") String password);

    @POST("api/addMotor")
    Call<Motor> addMotor(@Header("Authorization") String authorization,
                         @Body RequestBody file);

    @FormUrlEncoded
    @POST("api/addSales")
    Call<Sales> addSales(@Header("Authorization") String authorization,
                         @Field("username") String username,
                         @Field("password") String password,
                         @Field("nama") String nama,
                         @Field("alamat") String alamat,
                         @Field("no_wa") String no_wa,
                         @Field("no_ktp") String no_ktp);


    @FormUrlEncoded
    @POST("api/addMerk")
    Call<Merk> addMerk(@Header("Authorization") String authorization,
                       @Field("nama_merk") String nama_merk);


    @FormUrlEncoded
    @POST("api/addTipeMotor")
    Call<MerkTipe> addTipe(@Header("Authorization") String authorization,
                           @Field("id_merk") int id_merk,
                           @Field("nama_tipe") String nama_tipe);

    @FormUrlEncoded
    @POST("api/updateSales")
    Call<Sales> updateSales(@Header("Authorization") String authorization,
                            @Field("no_ktp_lama") String no_ktp_lama,
                            @Field("nama") String nama,
                            @Field("alamat") String alamat,
                            @Field("no_wa") String no_wa,
                            @Field("no_ktp") String no_ktp);


    @FormUrlEncoded
    @POST("api/updateMerk")
    Call<Merk> updatMerk(@Header("Authorization") String authorization,
                         @Field("id_merk_lama") int id_merk_lama,
                         @Field("nama_merk") String nama_merk);

    @FormUrlEncoded
    @POST("api/updateTipeMotor")
    Call<Tipe> updatTipeMotor(@Header("Authorization") String authorization,
                              @Field("id_tipe") int id_tipe_lama,
                              @Field("nama_tipe") String nama_tipe,
                              @Field("id_merk") int id_merk);


    @POST("api/updateMotor")
    Call<Motor> updateMotor(@Header("Authorization") String authorization,
                            @Body RequestBody file);

    @FormUrlEncoded
    @POST("api/delete")
    Call<Motor> delete(@Header("Authorization") String authorization,
                       @Field("no_mesin") String no_mesin,
                       @Field("gambar") String gambar,
                       @Field("gambar1") String gambar1,
                       @Field("gambar2") String gambar2);


    @FormUrlEncoded
    @POST("api/deleteSales")
    Call<Sales> deleteSales(@Header("Authorization") String authorization,
                            @Field("no_ktp_sales") String no_ktp_sales);


    @FormUrlEncoded
    @POST("api/deleteMerk")
    Call<Merk> deleteMerk(@Header("Authorization") String authorization,
                          @Field("id_merk") int id_merk);

    @FormUrlEncoded
    @POST("api/deleteTipeMotor")
    Call<MerkTipe> deleteTipe(@Header("Authorization") String authorization,
                              @Field("id_tipe") int id_tipe);

    @GET("api/detail/{no_mesin}")
    Call<Detail> getDetail(@Query("no_mesin") String no_mesin);


    @GET("api/getTipeMotor")
    Call<List<MerkTipe>> getTipeMotor();


    @POST("api/getPendingBeli")
    Call<List<PendingBeli>> getPendingBeli(@Query("id_user") String id_user);


    @POST("api/getPendingJual")
    Call<List<PendingJual>> getPendingJual(@Query("id_user") String id_user);


    @FormUrlEncoded
    @POST("api/addPendingBeli")
    Call<PendingBeli> addPendingBeli(@Header("Authorization") String authorization,
                                     @Field("id_user") int id_user,
                                     @Field("nama") String nama,
                                     @Field("alamat") String alamat,
                                     @Field("telepon") String telepon,
                                     @Field("id_merk") int id_merk,
                                     @Field("id_tipe") int id_tipe,
                                     @Field("tahun") String tahun,
                                     @Field("harga") String harga);

    @FormUrlEncoded
    @POST("api/addPendingJual")
    Call<PendingBeli> addPendingJual(@Header("Authorization") String authorization,
                                     @Field("id_user") int id_user,
                                     @Field("nama") String nama,
                                     @Field("alamat") String alamat,
                                     @Field("telepon") String telepon,
                                     @Field("id_merk") int id_merk,
                                     @Field("id_tipe") int id_tipe,
                                     @Field("no_mesin") String no_mesin,
                                     @Field("no_polisi") String no_polisi,
                                     @Field("tahun") String tahun,
                                     @Field("harga") String harga);


    @FormUrlEncoded
    @POST("api/updatePendingJual")
    Call<PendingBeli> updatePendingJual(@Header("Authorization") String authorization,
                                        @Field("id") int id,
                                        @Field("nama") String nama,
                                        @Field("alamat") String alamat,
                                        @Field("telepon") String telepon,
                                        @Field("id_merk") int id_merk,
                                        @Field("id_tipe") int id_tipe,
                                        @Field("no_mesin") String no_mesin,
                                        @Field("no_polisi") String no_polisi,
                                        @Field("tahun") String tahun,
                                        @Field("harga") String harga);

    @FormUrlEncoded
    @POST("api/deletePending")
    Call<PendingBeli> deletePending(@Header("Authorization") String authorization,
                                    @Field("id_pending") int id_pending);


    @FormUrlEncoded
    @POST("api/updatePending")
    Call<PendingBeli> updatePending(@Header("Authorization") String authorization,
                                    @Field("id_pending") int id_pending,
                                    @Field("nama") String nama,
                                    @Field("alamat") String alamat,
                                    @Field("telepon") String telepon,
                                    @Field("id_merk") int id_merk,
                                    @Field("id_tipe") int id_tipe,
                                    @Field("tahun") String tahun,
                                    @Field("harga") String harga);

    @FormUrlEncoded
    @POST("api/deletePendingJual")
    Call<PendingJual> deletePendingJual(@Header("Authorization") String authorization,
                                        @Field("id_pending") int id_pending);

    @FormUrlEncoded
    @POST("api/getProfile")
    Call<User> getProfile(@Header("Authorization") String authorization,
                          @Field("id_user") String id);


    @FormUrlEncoded
    @POST("api/editProfile")
    Call<User> updateProfileSales(@Header("Authorization") String authorization,
                                  @Field("id_user") String id_user,
                                  @Field("nama") String nama,
                                  @Field("alamat") String alamat,
                                  @Field("no_wa") String no_wa,
                                  @Field("no_ktp") String no_ktp,
                                  @Field("username") String username,
                                  @Field("password") String password,
                                  @Field("no_ktp_sales") String no_ktp_sales);


    @FormUrlEncoded
    @POST("api/editProfileOwner")
    Call<User> updateProfileOwner(@Header("Authorization") String authorization,
                                  @Field("id_user") String id_user,
                                  @Field("nama") String nama,
                                  @Field("alamat") String alamat,
                                  @Field("no_wa") String no_wa,
                                  @Field("no_ktp") String no_ktp,
                                  @Field("username") String username,
                                  @Field("password") String password,
                                  @Field("no_ktp_sales") String no_ktp_sales);


    @POST("api/getCustomer")
    Call<List<Customer>> getCustomer(@Query("id_user") String id_user);


    @GET("api/getCustomerById/{no_ktp}")
    Call<List<Customer>> getCustomerById(@Query("no_ktp") String no_ktp);

    @GET("api/getMotorById/{no_mesin}")
    Call<List<Motor>> getMotorById(@Query("no_mesin") String no_mesin);


    @FormUrlEncoded
    @POST("api/mokasWithNoCust")
    Call<Motor> mokasWithNoCust(@Header("Authorization") String authorization,
                                @Field("no_mesin") String no_mesin,
                                @Field("no_ktp") String no_ktp,
                                @Field("no_ktp_sales") String no_ktp_sales,
                                @Field("harga_terjual") String harga_terjual,
                                @Field("dp") String dp,
                                @Field("cicilan") String cicilan,
                                @Field("tenor") String tenor,
                                @Field("pencairanLeasing") String pencairanLeasing,
                                @Field("mediator") String mediator);


    @FormUrlEncoded
    @POST("api/mokasWithCust")
    Call<Motor> mokasWithCust(@Header("Authorization") String authorization,
                              @Field("no_mesin") String no_mesin,
                              @Field("no_ktp") String no_ktp,
                              @Field("nama") String nama,
                              @Field("alamat") String alamat,
                              @Field("no_telp") String no_telp,
                              @Field("ttl") String ttl,
                              @Field("agama") String agama,
                              @Field("pekerjaan") String pekerjaan,
                              @Field("whatsapp") String whatsapp,
                              @Field("instagram") String instagram,
                              @Field("facebook") String facebook,
                              @Field("harga_terjual") String harga_terjual,
                              @Field("dp") String dp,
                              @Field("cicilan") String cicilan,
                              @Field("tenor") String tenor,
                              @Field("pencairanLeasing") String pencairanLeasing,
                              @Field("no_ktp_sales") String no_ktp_sales,
                              @Field("mediator") String mediator);


    @FormUrlEncoded
    @POST("api/mobarWithNoCust")
    Call<Motor> mobarWithNoCust(@Header("Authorization") String authorization,
                                @Field("no_ktp") String no_ktp,
                                @Field("no_ktp_sales") String no_ktp_sales,
                                @Field("no_mesin") String no_mesin,
                                @Field("no_rangka") String no_rangka,
                                @Field("id_merk") String id_merk,
                                @Field("id_tipe") String id_tipe,
                                @Field("tahun") String tahun,
                                @Field("hjm") String hjm,
                                @Field("id_user") String id_user,
                                @Field("harga_terjual") String harga_terjual,
                                @Field("dp") String dp,
                                @Field("cicilan") String cicilan,
                                @Field("tenor") String tenor,
                                @Field("subsidi") String subsidi,
                                @Field("mediator") String mediator);


    @FormUrlEncoded
    @POST("api/mobarWithCust")
    Call<Motor> mobarWithCust(@Header("Authorization") String authorization,
                              @Field("no_mesin") String no_mesin,
                              @Field("no_rangka") String no_rangka,
                              @Field("tahun") String tahun,
                              @Field("hjm") String hjm,
                              @Field("id_tipe") String id_tipe,
                              @Field("id_merk") String id_merk,
                              @Field("id_user") String id_user,
                              @Field("harga_terjual") String harga_terjual,
                              @Field("dp") String dp,
                              @Field("cicilan") String cicilan,
                              @Field("tenor") String tenor,
                              @Field("subsidi") String subsidi,
                              @Field("no_ktp") String no_ktp,
                              @Field("nama") String nama,
                              @Field("alamat") String alamat,
                              @Field("no_telp") String no_telp,
                              @Field("Agama") String Agama,
                              @Field("pekerjaan") String pekerjaan,
                              @Field("whatsapp") String whatsapp,
                              @Field("instagram") String instagram,
                              @Field("facebook") String facebook,
                              @Field("no_ktp_sales") String no_ktp_sales,
                              @Field("ttl") String ttl,
                              @Field("mediator") String mediator);

    @POST("api/getTransaksi")
    Call<List<Transaksi>> getTransaksi(@Query("dari") String dari,
                                       @Query("ke") String ke,
                                       @Query("sales") String sales,
                                       @Query("kondisi") String kondisi,
                                       @Query("caraBayar") String caraBayar);

    @POST("api/getNamaCustomerAndNoTelp")
    Call<List<Customer>> getNamaCustomerAndNoTelp(@Query("no_mesin") String no_mesin);

    @POST("api/getFilteredMotor")
    Call<List<Motor>> getFilteredMotor(@Query("id_user") String id_user,
                                       @Query("status") String status,
                                       @Query("merk") String merk,
                                       @Query("tipe") String tipe,
                                       @Query("tahun") String tahun);

    @FormUrlEncoded
    @POST("api/searchCustomer")
    Call<List<Customer>> searchCustomer(@Field("id_user") String id_user,
                                        @Field("nama") String nama);


    @FormUrlEncoded
    @POST("api/searchNoPol")
    Call<List<Motor>> searchNoPol(@Field("no_polisi") String no);


    @FormUrlEncoded
    @POST("api/searchNoMesin")
    Call<List<Motor>> searchNoMesin(@Field("no_mesin") String no);

    @GET("api/getFilterSales")
    Call<List<Sales>> getFilterSales(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/searchPendingBeli")
    Call<List<PendingBeli>> searchPendingBeli(@Field("id_user") String id_user,
                                              @Field("nama") String nama);

    @FormUrlEncoded
    @POST("api/searchPendingJual")
    Call<List<PendingJual>> searchPendingJual(@Field("id_user") String id_user,
                                              @Field("nama") String nama);


    @POST("api/getFilteredPendingBeli")
    Call<List<PendingBeli>> getFilteredPendingBeli(@Query("id_user") String id_user,
                                                   @Query("merk") String merk,
                                                   @Query("tipe") String tipe);


    @POST("api/getFilteredPendingJual")
    Call<List<PendingJual>> getFilteredPendingJual(@Query("id_user") String id_user,
                                                   @Query("merk") String merk,
                                                   @Query("tipe") String tipe);


    @POST("api/updateCustomer")
    Call<Customer> updateCustomer(@Query("no_ktp") String noKTP,
                                  @Query("no_telp") String nomor,
                                  @Query("wa") String wa,
                                  @Query("fb") String fb,
                                  @Query("ig") String ig);

    @POST("api/motorValidate")
    Call<Motor> validateMotor(@Query("id_user") String id,
                              @Query("no_mesin") String nomorMesin);

    @FormUrlEncoded
    @POST("api/searchTipe")
    Call<List<MerkTipe>> searchTipe(@Field("nama_tipe") String namaTipe);

}
