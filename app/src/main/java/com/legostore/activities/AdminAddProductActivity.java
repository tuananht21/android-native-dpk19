package com.legostore.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.DanhMuc;
import com.legostore.models.PhanHoiApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddProductActivity extends AppCompatActivity {

    private ImageView btnQuayLai;
    private Spinner spnDanhMuc;
    private EditText edtTenSP, edtGia, edtSoLuong, edtMoTa;
    private Button btnLuu;

    private final List<DanhMuc> listDanhMuc = new ArrayList<>();
    private final List<String> listTenDanhMuc = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        btnQuayLai = findViewById(R.id.btnAdminAddQuayLai);
        spnDanhMuc = findViewById(R.id.spnAdminDanhMuc);
        edtTenSP = findViewById(R.id.edtAdminTenSP);
        edtGia = findViewById(R.id.edtAdminGiaBan);
        edtSoLuong = findViewById(R.id.edtAdminSoLuongTon);
        edtMoTa = findViewById(R.id.edtAdminMoTa);
        btnLuu = findViewById(R.id.btnAdminLuuSanPham);

        if (btnQuayLai != null) {
            btnQuayLai.setOnClickListener(v -> finish());
        }

        setupSpinnerDanhMuc();
        loadDanhMucTuServer();

        btnLuu.setOnClickListener(v -> xuLyThem());
    }

    private void setupSpinnerDanhMuc() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listTenDanhMuc);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDanhMuc.setAdapter(spinnerAdapter);
    }

    private void loadDanhMucTuServer() {
        RetrofitClient.getApiService().layDanhMuc().enqueue(new Callback<PhanHoiApi<List<DanhMuc>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<List<DanhMuc>>> call, Response<PhanHoiApi<List<DanhMuc>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    listDanhMuc.clear();
                    listTenDanhMuc.clear();

                    listDanhMuc.addAll(response.body().getDuLieu());
                    for (DanhMuc dm : listDanhMuc) {
                        listTenDanhMuc.add(dm.getTenDanhMuc());
                    }
                    spinnerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<List<DanhMuc>>> call, Throwable t) {
                Toast.makeText(AdminAddProductActivity.this, "Lỗi tải danh mục!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void xuLyThem() {
        String tenSP = edtTenSP.getText().toString().trim();
        String strGia = edtGia.getText().toString().trim();
        String strSoLuong = edtSoLuong.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();

        if (tenSP.isEmpty() || strGia.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên sản phẩm và giá bán!", Toast.LENGTH_SHORT).show();
            return;
        }

        int maDanhMuc = 1;
        int selectedIndex = spnDanhMuc.getSelectedItemPosition();
        if (selectedIndex >= 0 && selectedIndex < listDanhMuc.size()) {
            maDanhMuc = listDanhMuc.get(selectedIndex).getMaDanhMuc();
        }

        double gia = Double.parseDouble(strGia);
        int soLuong = strSoLuong.isEmpty() ? 10 : Integer.parseInt(strSoLuong);

        RetrofitClient.getApiService().themSanPhamAdmin(maDanhMuc, tenSP, gia, soLuong, moTa, "images/products/falcon.jpg")
                .enqueue(new Callback<PhanHoiApi<Void>>() {
                    @Override
                    public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(AdminAddProductActivity.this, response.body().getThongBao(), Toast.LENGTH_SHORT).show();
                            if (response.body().isThanhCong()) finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                        Toast.makeText(AdminAddProductActivity.this, "Lỗi thêm sản phẩm!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
