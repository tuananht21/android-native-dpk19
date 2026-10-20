package com.legostore.models;

import com.google.gson.annotations.SerializedName;

public class MucGioHang {
    @SerializedName("ma_gio_hang")
    private int maGioHang;

    @SerializedName("ma_san_pham")
    private int maSanPham;

    @SerializedName("ten_san_pham")
    private String tenSanPham;

    @SerializedName("gia_ban")
    private double giaBan;

    @SerializedName("hinh_anh")
    private String hinhAnh;

    @SerializedName("so_luong")
    private int soLuong;

    @SerializedName("thanh_tien")
    private double thanhTien;

    public int getMaGioHang() { return maGioHang; }
    public int getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public double getGiaBan() { return giaBan; }
    public String getHinhAnh() { return hinhAnh; }
    public int getSoLuong() { return soLuong; }
    public double getThanhTien() { return thanhTien; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}