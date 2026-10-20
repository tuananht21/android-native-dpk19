package com.legostore.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.PhanHoiApi;
import com.legostore.utils.UserSessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private EditText edtDiaChi, edtSdt, edtGhiChu;
    private Button btnXacNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        edtDiaChi = findViewById(R.id.edtDiaChiGiao);
        edtSdt = findViewById(R.id.edtSdtNhan);
        edtGhiChu = findViewById(R.id.edtGhiChu);
        btnXacNhan = findViewById(R.id.btnXacNhanDatHang);

        // Điền sẵn địa chỉ và SĐT đã lưu trong Session để khách đỡ gõ lại
        UserSessionManager session = new UserSessionManager(this);
        if (session.getAddress() != null && !session.getAddress().isEmpty()) {
            edtDiaChi.setText(session.getAddress());
        }
        if (session.getPhone() != null && !session.getPhone().isEmpty()) {
            edtSdt.setText(session.getPhone());
        }

        btnXacNhan.setOnClickListener(v -> xuLyDatHang());
    }

    private void xuLyDatHang() {
        String diaChi = edtDiaChi.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String ghiChu = edtGhiChu.getText().toString().trim();

        if (diaChi.isEmpty() || sdt.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ địa chỉ và SĐT!", Toast.LENGTH_SHORT).show();
            return;
        }

        UserSessionManager session = new UserSessionManager(this);
        int maNguoiDung = session.getUserId();

        RetrofitClient.getApiService().taoDonHang(maNguoiDung, diaChi, sdt, ghiChu).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    Toast.makeText(CheckoutActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CheckoutActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi kết nối máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
