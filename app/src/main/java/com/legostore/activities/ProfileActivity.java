package com.legostore.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.NguoiDung;
import com.legostore.models.PhanHoiApi;
import com.legostore.utils.UserSessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView btnProfileQuayLai;
    private TextView tvProfileHoTenHeader, tvProfileUserId, tvProfileEmail;
    private EditText edtProfileSdt, edtProfileDiaChi;
    private Button btnProfileCapNhatThongTin, btnProfileDoiMatKhau;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new UserSessionManager(this);

        initViews();

        btnProfileQuayLai.setOnClickListener(v -> finish());

        // Kiểm tra mã người dùng trước khi gọi API
        int userId = session.getUserId();
        Log.d("PROFILE_DEBUG", "Ma nguoi dung hien tai trong Session: " + userId);

        if (userId <= 0) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            hienThiTuSession();
        } else {
            // Lấy dữ liệu mới nhất trực tiếp từ CSDL MySQL khi mở màn hình
            taiThongTinTuCSDL(userId);
        }

        // Bấm nút Cập nhật để lưu thông tin mới vào CSDL
        btnProfileCapNhatThongTin.setOnClickListener(v -> luuThongTinVaoCSDL());

        // Bấm nút Đổi mật khẩu
        btnProfileDoiMatKhau.setOnClickListener(v -> hienThiDialogDoiMatKhau());
    }

    private void initViews() {
        btnProfileQuayLai = findViewById(R.id.btnProfileQuayLai);
        tvProfileHoTenHeader = findViewById(R.id.tvProfileHoTenHeader);
        tvProfileUserId = findViewById(R.id.tvProfileUserId);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        edtProfileSdt = findViewById(R.id.edtProfileSdt);
        edtProfileDiaChi = findViewById(R.id.edtProfileDiaChi);

        btnProfileCapNhatThongTin = findViewById(R.id.btnProfileCapNhatThongTin);
        btnProfileDoiMatKhau = findViewById(R.id.btnProfileDoiMatKhau);
    }

    // Tải dữ liệu chính xác từ CSDL MySQL thông qua Retrofit API
    private void taiThongTinTuCSDL(int userId) {
        RetrofitClient.getApiService().layThongTinNguoiDung(userId)
                .enqueue(new Callback<PhanHoiApi<NguoiDung>>() {
                    @Override
                    public void onResponse(Call<PhanHoiApi<NguoiDung>> call, Response<PhanHoiApi<NguoiDung>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                            NguoiDung user = response.body().getDuLieu();
                            if (user != null) {
                                tvProfileHoTenHeader.setText(user.getHoTen() != null && !user.getHoTen().isEmpty() ? user.getHoTen() : session.getFullName());
                                tvProfileUserId.setText("#" + user.getMaNguoiDung());
                                tvProfileEmail.setText(user.getEmail());

                                // Đổ SĐT từ CSDL ra EditText (nếu null thì để chuỗi rỗng)
                                String sdt = user.getSoDienThoai();
                                edtProfileSdt.setText(sdt != null ? sdt : "");

                                // Đổ Địa chỉ từ CSDL ra EditText
                                String diaChi = user.getDiaChi();
                                edtProfileDiaChi.setText(diaChi != null ? diaChi : "");

                                // Đồng bộ ngược lại vào Session
                                session.saveUserPhoneAndAddress(
                                        sdt != null ? sdt : "",
                                        diaChi != null ? diaChi : ""
                                );
                            }
                        } else {
                            Log.e("PROFILE_DEBUG", "Loi API lay thong tin nguoi dung");
                            hienThiTuSession();
                        }
                    }

                    @Override
                    public void onFailure(Call<PhanHoiApi<NguoiDung>> call, Throwable t) {
                        Log.e("PROFILE_DEBUG", "Loi ket noi: " + t.getMessage());
                        hienThiTuSession();
                    }
                });
    }

    private void hienThiTuSession() {
        tvProfileHoTenHeader.setText(session.getFullName());
        tvProfileUserId.setText("#" + session.getUserId());
        tvProfileEmail.setText(session.getEmail());

        edtProfileSdt.setText(session.getPhone());
        edtProfileDiaChi.setText(session.getAddress());
    }

    // Lưu dữ liệu gõ từ EditText ngược lại CSDL MySQL
    private void luuThongTinVaoCSDL() {
        int userId = session.getUserId();
        if (userId <= 0) {
            Toast.makeText(this, "Không xác định được tài khoản! Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        String sdtMoi = edtProfileSdt.getText().toString().trim();
        String diaChiMoi = edtProfileDiaChi.getText().toString().trim();

        if (sdtMoi.isEmpty() && diaChiMoi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Số điện thoại hoặc Địa chỉ để cập nhật!", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().capNhatThongTin(userId, sdtMoi, diaChiMoi).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProfileActivity.this, response.body().getThongBao(), Toast.LENGTH_SHORT).show();

                    if (response.body().isThanhCong()) {
                        // Lưu thông tin mới vào Session Manager
                        session.saveUserPhoneAndAddress(sdtMoi, diaChiMoi);
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Log.e("PROFILE_DEBUG", "Loi cap nhat: " + t.getMessage(), t);
                String msg = t.getMessage() != null ? t.getMessage() : "Không thể kết nối";
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hienThiDialogDoiMatKhau() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi mật khẩu");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_doi_mat_khau, null);
        EditText edtPassCu = view.findViewById(R.id.edtMatKhauCu);
        EditText edtPassMoi = view.findViewById(R.id.edtMatKhauMoi);

        builder.setView(view);
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String matKhauCu = edtPassCu.getText().toString().trim();
            String matKhauMoi = edtPassMoi.getText().toString().trim();

            if (!matKhauCu.isEmpty() && !matKhauMoi.isEmpty()) {
                RetrofitClient.getApiService().doiMatKhau(session.getUserId(), matKhauCu, matKhauMoi)
                        .enqueue(new Callback<PhanHoiApi<Void>>() {
                            @Override
                            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(ProfileActivity.this, response.body().getThongBao(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this, "Lỗi kết nối máy chủ!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(ProfileActivity.this, "Vui lòng nhập đầy đủ mật khẩu cũ và mới!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
