package com.yayanheryanto.larismotor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail implements Parcelable {

    @SerializedName("no_mesin")
    @Expose
    private String noMesin;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("dp")
    @Expose
    private String dp;
    @SerializedName("cicilan")
    @Expose
    private String cicilan;
    @SerializedName("tenor")
    @Expose
    private String tenor;
    @SerializedName("no_polisi")
    @Expose
    private String noPolisi;
    @SerializedName("no_rangka")
    @Expose
    private String noRangka;
    @SerializedName("hjm")
    @Expose
    private String hjm;
    @SerializedName("kondisi")
    @Expose
    private String kondisi;
    @SerializedName("tahun")
    @Expose
    private String tahun;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("harga_terjual")
    @Expose
    private String hargaTerjual;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("gambar1")
    @Expose
    private String gambar1;
    @SerializedName("gambar2")
    @Expose
    private String gambar2;
    @SerializedName("nama_merk")
    @Expose
    private String namaMerk;
    @SerializedName("nama_tipe")
    @Expose
    private String namaTipe;
    @SerializedName("no_wa")
    @Expose
    private String noWa;
    @SerializedName("subsidi")
    @Expose
    private String subsidi;
    @SerializedName("pencairan_leasing")
    @Expose
    private String pencairanLeasing;
    @SerializedName("nama")
    @Expose
    private String namaSales;

    public String getNamaSales() {
        return namaSales;
    }

    public String getNoMesin() {
        return noMesin;
    }

    public void setNoMesin(String noMesin) {
        this.noMesin = noMesin;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getCicilan() {
        return cicilan;
    }

    public void setCicilan(String cicilan) {
        this.cicilan = cicilan;
    }

    public String getTenor() {
        return tenor;
    }

    public void setTenor(String tenor) {
        this.tenor = tenor;
    }

    public String getNoPolisi() {
        return noPolisi;
    }

    public void setNoPolisi(String noPolisi) {
        this.noPolisi = noPolisi;
    }

    public String getNoRangka() {
        return noRangka;
    }

    public void setNoRangka(String noRangka) {
        this.noRangka = noRangka;
    }

    public String getHjm() {
        return hjm;
    }

    public void setHjm(String hjm) {
        this.hjm = hjm;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHargaTerjual() {
        return hargaTerjual;
    }

    public void setHargaTerjual(String hargaTerjual) {
        this.hargaTerjual = hargaTerjual;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getGambar1() {
        return gambar1;
    }

    public void setGambar1(String gambar1) {
        this.gambar1 = gambar1;
    }

    public String getGambar2() {
        return gambar2;
    }

    public void setGambar2(String gambar2) {
        this.gambar2 = gambar2;
    }

    public String getNamaMerk() {
        return namaMerk;
    }

    public void setNamaMerk(String namaMerk) {
        this.namaMerk = namaMerk;
    }

    public String getNamaTipe() {
        return namaTipe;
    }

    public void setNamaTipe(String namaTipe) {
        this.namaTipe = namaTipe;
    }

    public String getNoWa() {
        return noWa;
    }

    public void setNoWa(String noWa) {
        this.noWa = noWa;
    }

    public String getSubsidi() {
        return subsidi;
    }

    public void setSubsidi(String subsidi) {
        this.subsidi = subsidi;
    }

    public String getPencairanLeasing() {
        return pencairanLeasing;
    }

    public void setPencairanLeasing(String pencairanLeasing) {
        this.pencairanLeasing = pencairanLeasing;
    }


    protected Detail(Parcel in) {
        noMesin = in.readString();
        harga = in.readString();
        dp = in.readString();
        cicilan = in.readString();
        tenor = in.readString();
        noPolisi = in.readString();
        noRangka = in.readString();
        hjm = in.readString();
        kondisi = in.readString();
        tahun = in.readString();
        status = in.readString();
        hargaTerjual = in.readString();
        gambar = in.readString();
        gambar1 = in.readString();
        gambar2 = in.readString();
        namaMerk = in.readString();
        namaTipe = in.readString();
        noWa = in.readString();
        subsidi = in.readString();
        pencairanLeasing = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noMesin);
        dest.writeString(harga);
        dest.writeString(dp);
        dest.writeString(cicilan);
        dest.writeString(tenor);
        dest.writeString(noPolisi);
        dest.writeString(noRangka);
        dest.writeString(hjm);
        dest.writeString(kondisi);
        dest.writeString(tahun);
        dest.writeString(status);
        dest.writeString(hargaTerjual);
        dest.writeString(gambar);
        dest.writeString(gambar1);
        dest.writeString(gambar2);
        dest.writeString(namaMerk);
        dest.writeString(namaTipe);
        dest.writeString(noWa);
        dest.writeString(subsidi);
        dest.writeString(pencairanLeasing);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Detail> CREATOR = new Parcelable.Creator<Detail>() {
        @Override
        public Detail createFromParcel(Parcel in) {
            return new Detail(in);
        }

        @Override
        public Detail[] newArray(int size) {
            return new Detail[size];
        }
    };
}

