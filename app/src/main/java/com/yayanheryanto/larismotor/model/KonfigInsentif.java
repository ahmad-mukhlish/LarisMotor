package com.yayanheryanto.larismotor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KonfigInsentif {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("persentase")
    @Expose
    private String persentase;
    @SerializedName("insentif_mobar")
    @Expose
    private String insentifMobar;
    @SerializedName("insentif_mokas15")
    @Expose
    private String insentifMokas15;
    @SerializedName("insentif_mokas610")
    @Expose
    private String insentifMokas610;
    @SerializedName("insentif_mokas1115")
    @Expose
    private String insentifMokas1115;
    @SerializedName("insentif_mokas1620")
    @Expose
    private String insentifMokas1620;
    @SerializedName("insentif_mokas21")
    @Expose
    private String insentifMokas21;

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getPersentase() {
        return persentase;
    }

    public void setPersentase(String persentase) {
        this.persentase = persentase;
    }

    public String getInsentifMobar() {
        return insentifMobar;
    }

    public void setInsentifMobar(String insentifMobar) {
        this.insentifMobar = insentifMobar;
    }

    public String getInsentifMokas15() {
        return insentifMokas15;
    }

    public void setInsentifMokas15(String insentifMokas15) {
        this.insentifMokas15 = insentifMokas15;
    }

    public String getInsentifMokas610() {
        return insentifMokas610;
    }

    public void setInsentifMokas610(String insentifMokas610) {
        this.insentifMokas610 = insentifMokas610;
    }

    public String getInsentifMokas1115() {
        return insentifMokas1115;
    }

    public void setInsentifMokas1115(String insentifMokas1115) {
        this.insentifMokas1115 = insentifMokas1115;
    }

    public String getInsentifMokas1620() {
        return insentifMokas1620;
    }

    public void setInsentifMokas1620(String insentifMokas1620) {
        this.insentifMokas1620 = insentifMokas1620;
    }

    public String getInsentifMokas21() {
        return insentifMokas21;
    }

    public void setInsentifMokas21(String insentifMokas21) {
        this.insentifMokas21 = insentifMokas21;
    }
}