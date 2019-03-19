package com.example.toant.googlemap.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by toant on 13/06/2018.
 */

public class CityDistrict {

    @SerializedName("idQuanHuyen")
    public String idQuanHuyen;
    @SerializedName("tenQuanHuyen")
    public String tenQuanHuyen;
    @SerializedName("idTinhThanh")
    public String idTinhThanh;
    @SerializedName("tenTinhThanh")
    public String tenTinhThanh;

    public CityDistrict() {
    }

    public CityDistrict(String idQuanHuyen, String tenQuanHuyen, String idTinhThanh, String tenTinhThanh) {
        this.idQuanHuyen = idQuanHuyen;
        this.tenQuanHuyen = tenQuanHuyen;
        this.idTinhThanh = idTinhThanh;
        this.tenTinhThanh = tenTinhThanh;
    }

    public String getIdQuanHuyen() {
        return idQuanHuyen;
    }

    public void setIdQuanHuyen(String idQuanHuyen) {
        this.idQuanHuyen = idQuanHuyen;
    }

    public String getTenQuanHuyen() {
        return tenQuanHuyen;
    }

    public void setTenQuanHuyen(String tenQuanHuyen) {
        this.tenQuanHuyen = tenQuanHuyen;
    }

    public String getIdTinhThanh() {
        return idTinhThanh;
    }

    public void setIdTinhThanh(String idTinhThanh) {
        this.idTinhThanh = idTinhThanh;
    }

    public String getTenTinhThanh() {
        return tenTinhThanh;
    }

    public void setTenTinhThanh(String tenTinhThanh) {
        this.tenTinhThanh = tenTinhThanh;
    }
}
