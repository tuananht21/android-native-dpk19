package com.legostore.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.PhanHoiApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtHoTen, edtEmail, edtSdt, edtMatKhau;
    private Button btnDangKy;
    private TextView tvQuayLaiDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtHoTen = findViewById(R.id.edtHoTenDK);
        edtEmail = findViewById(R.id.edtEmailDK);
        edtSdt = findViewById(R.id.edtSdtDK);
        edtMatKhau = findViewById(R.id.edtMatKhauDK);
        btnDangKy = findViewById(R.id.btnDangKy);
        tvQuayLaiDangNhap = findViewById(R.id.tvQuayLaiDangNhap);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangKy();
            }
        });

        tvQuayLaiDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void xuLyDangKy() {
        String hoTen = edtHoTen.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        if (hoTen.isEmpty() || email.isEmpty() || sdt.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().dangKy(hoTen, email, sdt, matKhau).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PhanHoiApi<Void> phanHoi = response.body();
                    Toast.makeText(RegisterActivity.this, phanHoi.getThongBao(), Toast.LENGTH_SHORT).show();

                    if (phanHoi.isThanhCong()) {
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}