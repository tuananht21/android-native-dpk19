package com.legostore.models;

import com.google.gson.annotations.SerializedName;

public class SanPham {
    @SerializedName("ma_san_pham")
    private int maSanPham;

    @SerializedName("ma_danh_muc")
    private int maDanhMuc;

    @SerializedName("ten_danh_muc")
    private String tenDanhMuc;

    @SerializedName("ten_san_pham")
    private String tenSanPham;

    @SerializedName("gia_ban")
    private double giaBan;

    @SerializedName("so_luong_ton")
    private int soLuongTon;

    @SerializedName("hinh_anh")
    private String hinhAnh;

    @SerializedName("mo_ta")
    private String moTa;

    public int getMaSanPham() { return maSanPham; }
    public int getMaDanhMuc() { return maDanhMuc; }
    public String getTenDanhMuc() { return tenDanhMuc; }
    public String getTenSanPham() { return tenSanPham; }
    public double getGiaBan() { return giaBan; }
    public int getSoLuongTon() { return soLuongTon; }
    public String getHinhAnh() { return hinhAnh; }
    public String getMoTa() { return moTa; }
}