package com.yayanheryanto.larismotor.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingBeli implements Parcelable {


    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("id_pending")
    @Expose
    private Integer idPending;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("no_telp")
    @Expose
    private String noTelp;
    @SerializedName("tahun")
    @Expose
    private Integer tahun;
    @SerializedName("harga")
    @Expose
    private Integer harga;
    @SerializedName("nama_merk")
    @Expose
    private String namaMerk;
    @SerializedName("id_merk")
    @Expose
    private Integer idMerk;
    @SerializedName("nama_tipe")
    @Expose
    private String namaTipe;
    @SerializedName("id_tipe")
    @Expose
    private Integer idTipe;
    @SerializedName("tanggal_beli")
    @Expose
    private String tanggalBeli;

    public String getTanggalBeli() {
        return tanggalBeli;
    }

    public void setTanggalBeli(String tanggalBeli) {
        this.tanggalBeli = tanggalBeli;
    }



    public PendingBeli() {
    }

    public Integer getIdPending() {
        return idPending;
    }

    public void setIdPending(Integer idPending) {
        this.idPending = idPending;
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

    public Integer getTahun() {
        return tahun;
    }

    public void setTahun(Integer tahun) {
        this.tahun = tahun;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public String getNamaMerk() {
        return namaMerk;
    }

    public void setNamaMerk(String namaMerk) {
        this.namaMerk = namaMerk;
    }

    public Integer getIdMerk() {
        return idMerk;
    }

    public void setIdMerk(Integer idMerk) {
        this.idMerk = idMerk;
    }

    public String getNamaTipe() {
        return namaTipe;
    }

    public void setNamaTipe(String namaTipe) {
        this.namaTipe = namaTipe;
    }

    public Integer getIdTipe() {
        return idTipe;
    }

    public void setIdTipe(Integer idTipe) {
        this.idTipe = idTipe;
    }




    protected PendingBeli(Parcel in) {
        message = in.readString();
        idPending = in.readByte() == 0x00 ? null : in.readInt();
        nama = in.readString();
        alamat = in.readString();
        noTelp = in.readString();
        tahun = in.readByte() == 0x00 ? null : in.readInt();
        harga = in.readByte() == 0x00 ? null : in.readInt();
        namaMerk = in.readString();
        idMerk = in.readByte() == 0x00 ? null : in.readInt();
        namaTipe = in.readString();
        idTipe = in.readByte() == 0x00 ? null : in.readInt();
        tanggalBeli = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        if (idPending == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(idPending);
        }
        dest.writeString(nama);
        dest.writeString(alamat);
        dest.writeString(noTelp);
        if (tahun == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(tahun);
        }
        if (harga == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(harga);
        }
        dest.writeString(namaMerk);
        if (idMerk == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(idMerk);
        }
        dest.writeString(namaTipe);
        if (idTipe == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(idTipe);
        }
        dest.writeString(tanggalBeli);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PendingBeli> CREATOR = new Parcelable.Creator<PendingBeli>() {
        @Override
        public PendingBeli createFromParcel(Parcel in) {
            return new PendingBeli(in);
        }

        @Override
        public PendingBeli[] newArray(int size) {
            return new PendingBeli[size];
        }
    };
}