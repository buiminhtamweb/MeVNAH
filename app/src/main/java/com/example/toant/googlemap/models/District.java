package com.example.toant.googlemap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by toant on 05/06/2018.
 */

public class District {

    @Expose
    @SerializedName("tenQuanHuyen")
    private String tenQuanHuyen;
    @Expose
    @SerializedName("idQuanHuyen")
    private String idQuanHuyen;
    @Expose
    @SerializedName("idTinhThanh")
    private String idTinhThanh;

    public District(){}

    public District(String tenQuanHuyen, String idQuanHuyen, String idTinhThanh) {
        this.tenQuanHuyen = tenQuanHuyen;
        this.idQuanHuyen = idQuanHuyen;
        this.idTinhThanh = idTinhThanh;
    }

    public String getTenQuanHuyen() {
        return tenQuanHuyen;
    }

    public void setTenQuanHuyen(String tenQuanHuyen) {
        this.tenQuanHuyen = tenQuanHuyen;
    }

    public String getIdQuanHuyen() {
        return idQuanHuyen;
    }

    public void setIdQuanHuyen(String idQuanHuyen) {
        this.idQuanHuyen = idQuanHuyen;
    }

    public String getIdTinhThanh() {
        return idTinhThanh;
    }

    public void setIdTinhThanh(String idTinhThanh) {
        this.idTinhThanh = idTinhThanh;
    }
}
