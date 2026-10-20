package com.legostore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.legostore.MainActivity;
import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.NguoiDung;
import com.legostore.models.PhanHoiApi;
import com.legostore.utils.UserSessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtMatKhau;
    private Button btnDangNhap;
    private TextView tvChuyenSangDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtMatKhau = findViewById(R.id.edtMatKhauLogin);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvChuyenSangDangKy = findViewById(R.id.tvChuyenSangDangKy);

        btnDangNhap.setOnClickListener(v -> xuLyDangNhap());

        tvChuyenSangDangKy.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }

    private void xuLyDangNhap() {
        String email = edtEmail.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        if (email.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().dangNhap(email, matKhau).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PhanHoiApi<Void> phanHoi = response.body();

                    if (phanHoi.isThanhCong()) {
                        Toast.makeText(LoginActivity.this, phanHoi.getThongBao(), Toast.LENGTH_SHORT).show();

                        String userRole = "USER";
                        if (phanHoi.getVaiTro() != null && !phanHoi.getVaiTro().isEmpty()) {
                            userRole = phanHoi.getVaiTro().toUpperCase();
                        } else if (email.toLowerCase().contains("admin") ||
                                (phanHoi.getThongBao() != null && phanHoi.getThongBao().toLowerCase().contains("admin"))) {
                            userRole = "ADMIN";
                        }

                        String hoTenUser = phanHoi.getHoTen() != null ? phanHoi.getHoTen() : "";

                        NguoiDung user = new NguoiDung(
                                phanHoi.getMaNguoiDung(),
                                hoTenUser, email, "", "", userRole
                        );

                        UserSessionManager session = new UserSessionManager(LoginActivity.this);
                        session.saveUser(user);

                        if (session.isAdmin()) {
                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, phanHoi.getThongBao(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}