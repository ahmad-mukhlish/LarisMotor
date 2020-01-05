package com.yayanheryanto.larismotor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Motor implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("no_mesin")
    @Expose
    private String noMesin;
    @SerializedName("id_merk")
    @Expose
    private Integer idMerk;
    @SerializedName("id_tipe")
    @Expose
    private Integer idTipe;
    @SerializedName("id_user")
    @Expose
    private Integer idUser;
    @SerializedName("no_polisi")
    @Expose
    private String noPolisi;
    @SerializedName("no_rangka")
    @Expose
    private String noRangka;
    @SerializedName("tahun")
    @Expose
    private Integer tahun;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("kondisi")
    @Expose
    private Integer kondisi;
    @SerializedName("harga")
    @Expose
    private Integer harga;
    @SerializedName("hjm")
    @Expose
    private Integer hjm;
    @SerializedName("harga_terjual")
    @Expose
    private Integer hargaTerjual;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("gambar1")
    @Expose
    private String gambar1;
    @SerializedName("gambar2")
    @Expose
    private String gambar2;
    @SerializedName("cicilan")
    @Expose
    private Integer cicilan;
    @SerializedName("tenor")
    @Expose
    private Integer tenor;
    @SerializedName("dp")
    @Expose
    private Integer dp;
    @SerializedName("subsidi")
    @Expose
    private Integer subsidi;
    @SerializedName("pencairan_leasing")
    @Expose
    private Integer pencairanLeasing;
    @SerializedName("mediator")
    @Expose
    private Integer mediator;
    @SerializedName("approved")
    @Expose
    private Integer approved;

    public Motor() {
    }



    public String getNoMesin() {
        return noMesin;
    }

    public void setNoMesin(String noMesin) {
        this.noMesin = noMesin;
    }

    public Integer getIdMerk() {
        return idMerk;
    }

    public void setIdMerk(Integer idMerk) {
        this.idMerk = idMerk;
    }

    public Integer getIdTipe() {
        return idTipe;
    }

    public void setIdTipe(Integer idTipe) {
        this.idTipe = idTipe;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
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

    public Integer getTahun() {
        return tahun;
    }

    public void setTahun(Integer tahun) {
        this.tahun = tahun;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getKondisi() {
        return kondisi;
    }

    public void setKondisi(Integer kondisi) {
        this.kondisi = kondisi;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public Integer getHjm() {
        return hjm;
    }

    public void setHjm(Integer hjm) {
        this.hjm = hjm;
    }

    public Integer getHargaTerjual() {
        return hargaTerjual;
    }

    public void setHargaTerjual(Integer hargaTerjual) {
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

    public Integer getCicilan() {
        return cicilan;
    }

    public void setCicilan(Integer cicilan) {
        this.cicilan = cicilan;
    }

    public Integer getTenor() {
        return tenor;
    }

    public void setTenor(Integer tenor) {
        this.tenor = tenor;
    }

    public Integer getDp() {
        return dp;
    }

    public void setDp(Integer dp) {
        this.dp = dp;
    }

    public Integer getSubsidi() {
        return subsidi;
    }

    public void setSubsidi(Integer subsidi) {
        this.subsidi = subsidi;
    }

    public Integer getPencairanLeasing() {
        return pencairanLeasing;
    }

    public void setPencairanLeasing(Integer pencairanLeasing) {
        this.pencairanLeasing = pencairanLeasing;
    }

    public Integer getMediator() {
        return mediator;
    }

    public void setMediator(Integer mediator) {
        this.mediator = mediator;
    }


    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return super.toString();
    }


    protected Motor(Parcel in) {
        message = in.readString();
        noMesin = in.readString();
        idMerk = in.readByte() == 0x00 ? null : in.readInt();
        idTipe = in.readByte() == 0x00 ? null : in.readInt();
        idUser = in.readByte() == 0x00 ? null : in.readInt();
        noPolisi = in.readString();
        noRangka = in.readString();
        tahun = in.readByte() == 0x00 ? null : in.readInt();
        status = in.readByte() == 0x00 ? null : in.readInt();
        kondisi = in.readByte() == 0x00 ? null : in.readInt();
        harga = in.readByte() == 0x00 ? null : in.readInt();
        hjm = in.readByte() == 0x00 ? null : in.readInt();
        hargaTerjual = in.readByte() == 0x00 ? null : in.readInt();
        gambar = in.readString();
        gambar1 = in.readString();
        gambar2 = in.readString();
        cicilan = in.readByte() == 0x00 ? null : in.readInt();
        tenor = in.readByte() == 0x00 ? null : in.readInt();
        dp = in.readByte() == 0x00 ? null : in.readInt();
        subsidi = in.readByte() == 0x00 ? null : in.readInt();
        pencairanLeasing = in.readByte() == 0x00 ? null : in.readInt();
        mediator = in.readByte() == 0x00 ? null : in.readInt();
        approved = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(noMesin);
        if (idMerk == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(idMerk);
        }
        if (idTipe == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(idTipe);
        }
        if (idUser == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(idUser);
        }
        dest.writeString(noPolisi);
        dest.writeString(noRangka);
        if (tahun == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(tahun);
        }
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
        if (kondisi == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(kondisi);
        }
        if (harga == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(harga);
        }
        if (hjm == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(hjm);
        }
        if (hargaTerjual == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(hargaTerjual);
        }
        dest.writeString(gambar);
        dest.writeString(gambar1);
        dest.writeString(gambar2);
        if (cicilan == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cicilan);
        }
        if (tenor == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(tenor);
        }
        if (dp == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(dp);
        }
        if (subsidi == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(subsidi);
        }
        if (pencairanLeasing == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(pencairanLeasing);
        }
        if (mediator == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mediator);
        }
        if (approved == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(approved);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Motor> CREATOR = new Parcelable.Creator<Motor>() {
        @Override
        public Motor createFromParcel(Parcel in) {
            return new Motor(in);
        }

        @Override
        public Motor[] newArray(int size) {
            return new Motor[size];
        }
    };
}