package com.example.toant.googlemap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Xa {


    @SerializedName("tenPhuongXa")
    public String tenPhuongXa;

    @SerializedName("idPhuongXa")
    public String idPhuongXa;

    @SerializedName("idQuanHuyen")
    public String idQuanHuyen;

    public Xa() {
    }

    public Xa(String tenPhuongXa, String idPhuongXa, String idQuanHuyen) {
        this.tenPhuongXa = tenPhuongXa;
        this.idPhuongXa = idPhuongXa;
        this.idQuanHuyen = idQuanHuyen;
    }


    public String getTenPhuongXa() {
        return tenPhuongXa;
    }

    public void setTenPhuongXa(String tenPhuongXa) {
        this.tenPhuongXa = tenPhuongXa;
    }

    public String getIdPhuongXa() {
        return idPhuongXa;
    }

    public void setIdPhuongXa(String idPhuongXa) {
        this.idPhuongXa = idPhuongXa;
    }

    public String getIdQuanHuyen() {
        return idQuanHuyen;
    }

    public void setIdQuanHuyen(String idQuanHuyen) {
        this.idQuanHuyen = idQuanHuyen;
    }
}
