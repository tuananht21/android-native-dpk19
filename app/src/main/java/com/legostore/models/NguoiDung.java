package com.legostore.models;

import com.google.gson.annotations.SerializedName;

public class NguoiDung {
    @SerializedName("ma_nguoi_dung")
    private int maNguoiDung;

    @SerializedName("ho_ten")
    private String hoTen;

    @SerializedName("email")
    private String email;

    @SerializedName("so_dien_thoai")
    private String soDienThoai;

    @SerializedName("dia_chi")
    private String diaChi;

    @SerializedName("vai_tro")
    private String vaiTro;

    public NguoiDung() {}

    public NguoiDung(int maNguoiDung, String hoTen, String email, String soDienThoai, String diaChi, String vaiTro) {
        this.maNguoiDung = maNguoiDung;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
    }

    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
}