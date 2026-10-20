package com.legostore.api;

import com.legostore.models.DanhMuc;
import com.legostore.models.DonHang;
import com.legostore.models.MucGioHang;
import com.legostore.models.NguoiDung;
import com.legostore.models.PhanHoiApi;
import com.legostore.models.SanPham;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("dang_ky.php")
    Call<PhanHoiApi<Void>> dangKy(
            @Field("ho_ten") String hoTen,
            @Field("email") String email,
            @Field("so_dien_thoai") String soDienThoai,
            @Field("mat_khau") String matKhau
    );

    @FormUrlEncoded
    @POST("dang_nhap.php")
    Call<PhanHoiApi<Void>> dangNhap(
            @Field("email") String email,
            @Field("mat_khau") String matKhau
    );

    @GET("lay_danh_muc.php")
    Call<PhanHoiApi<List<DanhMuc>>> layDanhMuc();

    @GET("lay_san_pham.php")
    Call<PhanHoiApi<List<SanPham>>> laySanPham(
            @Query("trang") int trang,
            @Query("ma_danh_muc") int maDanhMuc,
            @Query("tu_khoa") String tuKhoa
    );

    @FormUrlEncoded
    @POST("them_gio_hang.php")
    Call<PhanHoiApi<Void>> themGioHang(
            @Field("ma_nguoi_dung") int maNguoiDung,
            @Field("ma_san_pham") int maSanPham,
            @Field("so_luong") int soLuong
    );

    @GET("lay_gio_hang.php")
    Call<PhanHoiApi<List<MucGioHang>>> layGioHang(
            @Query("ma_nguoi_dung") int maNguoiDung
    );

    @FormUrlEncoded
    @POST("cap_nhat_gio_hang.php")
    Call<PhanHoiApi<Void>> capNhatGioHang(
            @Field("ma_nguoi_dung") int maNguoiDung,
            @Field("ma_gio_hang") int maGioHang,
            @Field("so_luong") int soLuong
    );

    @FormUrlEncoded
    @POST("xoa_gio_hang.php")
    Call<PhanHoiApi<Void>> xoaGioHang(
            @Field("ma_nguoi_dung") int maNguoiDung,
            @Field("ma_gio_hang") int maGioHang
    );

    @FormUrlEncoded
    @POST("tao_don_hang.php")
    Call<PhanHoiApi<Void>> taoDonHang(
            @Field("ma_nguoi_dung") int maNguoiDung,
            @Field("dia_chi_giao_hang") String diaChi,
            @Field("so_dien_thoai_nhan") String soDienThoai,
            @Field("ghi_chu") String ghiChu
    );

    @GET("lay_don_hang.php")
    Call<PhanHoiApi<List<DonHang>>> layDonHang(
            @Query("ma_nguoi_dung") int maNguoiDung
    );

    @FormUrlEncoded
    @POST("huy_don_hang.php")
    Call<PhanHoiApi<Void>> huyDonHang(
            @Field("ma_nguoi_dung") int maNguoiDung,
            @Field("ma_don_hang") int maDonHang
    );

    @FormUrlEncoded
    @POST("doi_mat_khau.php")
    Call<PhanHoiApi<Void>> doiMatKhau(
            @Field("ma_nguoi_dung") int maNguoiDung,
            @Field("mat_khau_cu") String matKhauCu,
            @Field("mat_khau_moi") String matKhauMoi
    );

    @FormUrlEncoded
    @POST("cap_nhat_thong_tin.php")
    Call<PhanHoiApi<Void>> capNhatThongTin(
            @Field("ma_nguoi_dung") int maNguoiDung,
            @Field("so_dien_thoai") String soDienThoai,
            @Field("dia_chi") String diaChi
    );

    @GET("lay_thong_tin_nguoi_dung.php")
    Call<PhanHoiApi<NguoiDung>> layThongTinNguoiDung(
            @Query("ma_nguoi_dung") int maNguoiDung
    );

    @FormUrlEncoded
    @POST("admin/them_san_pham.php")
    Call<PhanHoiApi<Void>> themSanPhamAdmin(
            @Field("ma_danh_muc") int maDanhMuc,
            @Field("ten_san_pham") String tenSanPham,
            @Field("gia_ban") double giaBan,
            @Field("so_luong_ton") int soLuongTon,
            @Field("mo_ta") String moTa,
            @Field("hinh_anh") String hinhAnh
    );

    @FormUrlEncoded
    @POST("admin/cap_nhat_don_hang.php")
    Call<PhanHoiApi<Void>> capNhatDonHangAdmin(
            @Field("ma_don_hang") int maDonHang,
            @Field("trang_thai") String trangThai
    );

    @GET("admin/thong_ke_doanh_thu.php")
    Call<PhanHoiApi<Map<String, Double>>> layThongKeDoanhThuAdmin();
}