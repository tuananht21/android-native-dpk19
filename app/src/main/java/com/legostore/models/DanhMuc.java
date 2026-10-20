package com.legostore.models;

import com.google.gson.annotations.SerializedName;

public class DanhMuc {
    @SerializedName("ma_danh_muc")
    private int maDanhMuc;

    @SerializedName("ten_danh_muc")
    private String tenDanhMuc;

    @SerializedName("hinh_anh")
    private String hinhAnh;

    @SerializedName("mo_ta")
    private String moTa;

    public int getMaDanhMuc() { return maDanhMuc; }
    public String getTenDanhMuc() { return tenDanhMuc; }
    public String getHinhAnh() { return hinhAnh; }
    public String getMoTa() { return moTa; }
}