package com.example.toant.googlemap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by toant on 04/06/2018.
 */

public class MapLocation {


    @Expose
    @SerializedName("photoListLocation")
    public String photoListLocation;
    @Expose
    @SerializedName("photoLocation")
    public String photoLocation;
    @Expose
    @SerializedName("longitudeLocation")
    public String longitudeLocation;
    @Expose
    @SerializedName("latitudeLocation")
    public String latitudeLocation;
    @Expose
    @SerializedName("noiDungLocation")
    public String noiDungLocation;
    @Expose
    @SerializedName("phoneLocation")
    public String phoneLocation;
    @Expose
    @SerializedName("diaChiLocation")
    public String diaChiLocation;
    @Expose
    @SerializedName("tenLocation")
    public String tenLocation;
    @Expose
    @SerializedName("tenTinhThanh")
    public String tenTinhThanh;
    @Expose
    @SerializedName("tenQuanHuyen")
    public String tenQuanHuyen;
    @Expose
    @SerializedName("tenPhuongXa")
    public String tenPhuongXa;
    @Expose
    @SerializedName("idPhuongXa")
    public String idPhuongXa;
    @Expose
    @SerializedName("idTypeLocation")
    public String idTypeLocation;
    @Expose
    @SerializedName("idLocation")
    public String idLocation;

    @SerializedName("kmLocation")
    public double kmLocation;

    public MapLocation(String photoListLocation, String photoLocation, String longitudeLocation, String latitudeLocation, String noiDungLocation, String phoneLocation, String diaChiLocation, String tenLocation, String tenTinhThanh, String tenQuanHuyen, String tenPhuongXa, String idPhuongXa, String idTypeLocation, String idLocation) {
        this.photoListLocation = photoListLocation;
        this.photoLocation = photoLocation;
        this.longitudeLocation = longitudeLocation;
        this.latitudeLocation = latitudeLocation;
        this.noiDungLocation = noiDungLocation;
        this.phoneLocation = phoneLocation;
        this.diaChiLocation = diaChiLocation;
        this.tenLocation = tenLocation;
        this.tenTinhThanh = tenTinhThanh;
        this.tenQuanHuyen = tenQuanHuyen;
        this.tenPhuongXa = tenPhuongXa;
        this.idPhuongXa = idPhuongXa;
        this.idTypeLocation = idTypeLocation;
        this.idLocation = idLocation;
    }

    public MapLocation(String photoListLocation, String photoLocation, String longitudeLocation, String latitudeLocation, String noiDungLocation, String phoneLocation, String diaChiLocation, String tenLocation, String tenTinhThanh, String tenQuanHuyen, String tenPhuongXa, String idPhuongXa, String idTypeLocation, String idLocation, double kmLocation) {
        this.photoListLocation = photoListLocation;
        this.photoLocation = photoLocation;
        this.longitudeLocation = longitudeLocation;
        this.latitudeLocation = latitudeLocation;
        this.noiDungLocation = noiDungLocation;
        this.phoneLocation = phoneLocation;
        this.diaChiLocation = diaChiLocation;
        this.tenLocation = tenLocation;
        this.tenTinhThanh = tenTinhThanh;
        this.tenQuanHuyen = tenQuanHuyen;
        this.tenPhuongXa = tenPhuongXa;
        this.idPhuongXa = idPhuongXa;
        this.idTypeLocation = idTypeLocation;
        this.idLocation = idLocation;
        this.kmLocation = kmLocation;
    }

    public String getPhotoListLocation() {
        return photoListLocation;
    }

    public String getPhotoLocation() {
        return photoLocation;
    }

    public String getLongitudeLocation() {
        return longitudeLocation;
    }

    public String getLatitudeLocation() {
        return latitudeLocation;
    }

    public String getNoiDungLocation() {
        return noiDungLocation;
    }

    public String getPhoneLocation() {
        return phoneLocation;
    }

    public String getDiaChiLocation() {
        return diaChiLocation;
    }

    public String getTenLocation() {
        return tenLocation;
    }

    public String getTenTinhThanh() {
        return tenTinhThanh;
    }

    public String getTenQuanHuyen() {
        return tenQuanHuyen;
    }

    public String getTenPhuongXa() {
        return tenPhuongXa;
    }

    public String getIdPhuongXa() {
        return idPhuongXa;
    }

    public String getIdTypeLocation() {
        return idTypeLocation;
    }

    public String getIdLocation() {
        return idLocation;
    }

    public void setPhotoListLocation(String photoListLocation) {
        this.photoListLocation = photoListLocation;
    }

    public void setPhotoLocation(String photoLocation) {
        this.photoLocation = photoLocation;
    }

    public void setLongitudeLocation(String longitudeLocation) {
        this.longitudeLocation = longitudeLocation;
    }

    public void setLatitudeLocation(String latitudeLocation) {
        this.latitudeLocation = latitudeLocation;
    }

    public void setNoiDungLocation(String noiDungLocation) {
        this.noiDungLocation = noiDungLocation;
    }

    public void setPhoneLocation(String phoneLocation) {
        this.phoneLocation = phoneLocation;
    }

    public void setDiaChiLocation(String diaChiLocation) {
        this.diaChiLocation = diaChiLocation;
    }

    public void setTenLocation(String tenLocation) {
        this.tenLocation = tenLocation;
    }

    public void setTenTinhThanh(String tenTinhThanh) {
        this.tenTinhThanh = tenTinhThanh;
    }

    public void setTenQuanHuyen(String tenQuanHuyen) {
        this.tenQuanHuyen = tenQuanHuyen;
    }

    public void setTenPhuongXa(String tenPhuongXa) {
        this.tenPhuongXa = tenPhuongXa;
    }

    public void setIdPhuongXa(String idPhuongXa) {
        this.idPhuongXa = idPhuongXa;
    }

    public void setIdTypeLocation(String idTypeLocation) {
        this.idTypeLocation = idTypeLocation;
    }

    public void setIdLocation(String idLocation) {
        this.idLocation = idLocation;
    }

    public double getKmLocation() {
        return kmLocation;
    }

    public void setKmLocation(double kmLocation) {
        this.kmLocation = kmLocation;
    }
}
