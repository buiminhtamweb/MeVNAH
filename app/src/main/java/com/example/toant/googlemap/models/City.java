package com.example.toant.googlemap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by toant on 05/06/2018.
 */

public class City {

    @Expose
    @SerializedName("tenTinhThanh")
    private String tenTinhThanh;
    @Expose
    @SerializedName("idTinhThanh")
    private String idTinhThanh;

    public City() {
    }

    public City(String tenTinhThanh, String idTinhThanh) {
        this.tenTinhThanh = tenTinhThanh;
        this.idTinhThanh = idTinhThanh;
    }

    public String getTenTinhThanh() {
        return tenTinhThanh;
    }

    public void setTenTinhThanh(String tenTinhThanh) {
        this.tenTinhThanh = tenTinhThanh;
    }

    public String getIdTinhThanh() {
        return idTinhThanh;
    }

    public void setIdTinhThanh(String idTinhThanh) {
        this.idTinhThanh = idTinhThanh;
    }
}
