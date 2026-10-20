package com.legostore.models;

import com.google.gson.annotations.SerializedName;

public class DonHang {
    @SerializedName("ma_don_hang")
    private int maDonHang;

    @SerializedName("tong_tien")
    private double tongTien;

    @SerializedName("dia_chi_giao_hang")
    private String diaChiGiaoHang;

    @SerializedName("so_dien_thoai_nhan")
    private String soDienThoaiNhan;

    @SerializedName("ghi_chu")
    private String ghiChu;

    @SerializedName("trang_thai")
    private String trangThai;

    public int getMaDonHang() { return maDonHang; }
    public double getTongTien() { return tongTien; }
    public String getDiaChiGiaoHang() { return diaChiGiaoHang; }
    public String getSoDienThoaiNhan() { return soDienThoaiNhan; }
    public String getGhiChu() { return ghiChu; }
    public String getTrangThai() { return trangThai; }
}