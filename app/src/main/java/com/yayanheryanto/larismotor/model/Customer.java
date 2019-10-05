package com.yayanheryanto.larismotor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("no_ktp")
    @Expose
    private String noKtp;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("no_telp")
    @Expose
    private String noTelp;
    @SerializedName("ttl")
    @Expose
    private String ttl;
    @SerializedName("Agama")
    @Expose
    private String agama;
    @SerializedName("pekerjaan")
    @Expose
    private String pekerjaan;
    @SerializedName("whatsapp")
    @Expose
    private String whatsapp;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("instagram")
    @Expose
    private String instagram;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("jumlah_pembelian")
    @Expose
    private String jumlahPembelian;

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getJumlahPembelian() {
        return jumlahPembelian;
    }

    public void setJumlahPembelian(String jumlahPembelian) {
        this.jumlahPembelian = jumlahPembelian;
    }


    protected Customer(Parcel in) {
        noKtp = in.readString();
        nama = in.readString();
        alamat = in.readString();
        noTelp = in.readString();
        ttl = in.readString();
        agama = in.readString();
        pekerjaan = in.readString();
        whatsapp = in.readString();
        email = in.readString();
        instagram = in.readString();
        facebook = in.readString();
        jumlahPembelian = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noKtp);
        dest.writeString(nama);
        dest.writeString(alamat);
        dest.writeString(noTelp);
        dest.writeString(ttl);
        dest.writeString(agama);
        dest.writeString(pekerjaan);
        dest.writeString(whatsapp);
        dest.writeString(email);
        dest.writeString(instagram);
        dest.writeString(facebook);
        dest.writeString(jumlahPembelian);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}