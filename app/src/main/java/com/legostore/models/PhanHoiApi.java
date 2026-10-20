package com.legostore.models;

import com.google.gson.annotations.SerializedName;

public class PhanHoiApi<T> {
    @SerializedName("trang_thai")
    private String trangThai;

    @SerializedName("thong_bao")
    private String thongBao;

    @SerializedName("du_lieu")
    private T duLieu;

    @SerializedName("ma_nguoi_dung")
    private int maNguoiDung;

    @SerializedName("ho_ten")
    private String hoTen;

    @SerializedName("ma_xac_thuc")
    private String maXacThuc;

    @SerializedName("vai_tro")
    private String vaiTro;

    @SerializedName("tong_tien_gio_hang")
    private double tongTienGioHang;

    @SerializedName("ma_don_hang")
    private int maDonHang;

    @SerializedName("tong_so_trang")
    private int tongSoTrang;

    public String getTrangThai() { return trangThai; }
    public String getThongBao() { return thongBao; }
    public T getDuLieu() { return duLieu; }
    public int getMaNguoiDung() { return maNguoiDung; }
    public String getHoTen() { return hoTen; }
    public String getMaXacThuc() { return maXacThuc; }
    public String getVaiTro() { return vaiTro; }
    public double getTongTienGioHang() { return tongTienGioHang; }
    public int getMaDonHang() { return maDonHang; }
    public int getTongSoTrang() { return tongSoTrang; }

    public boolean isThanhCong() {
        return "success".equalsIgnoreCase(trangThai);
    }
}
