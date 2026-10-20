package com.legostore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legostore.R;
import com.legostore.adapters.GioHangAdapter;
import com.legostore.api.RetrofitClient;
import com.legostore.models.MucGioHang;
import com.legostore.models.PhanHoiApi;
import com.legostore.utils.UserSessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private ImageView btnQuayLai;
    private RecyclerView rcvGioHang;
    private TextView tvTongTien;
    private Button btnDatHang;
    private LinearLayout layoutGioHangTrong;

    private GioHangAdapter adapter;
    private List<MucGioHang> list = new ArrayList<>();
    private int maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        UserSessionManager session = new UserSessionManager(this);
        maNguoiDung = session.getUserId();

        initViews();

        if (btnQuayLai != null) {
            btnQuayLai.setOnClickListener(v -> finish());
        }

        rcvGioHang.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GioHangAdapter(this, list, new GioHangAdapter.OnCartItemChangeListener() {
            @Override
            public void onTangSoLuong(MucGioHang item) {
                capNhatSoLuong(item.getMaGioHang(), item.getSoLuong() + 1);
            }

            @Override
            public void onGiamSoLuong(MucGioHang item) {
                capNhatSoLuong(item.getMaGioHang(), item.getSoLuong() - 1);
            }

            @Override
            public void onXoaItem(MucGioHang item) {
                xoaGioHang(item.getMaGioHang());
            }
        });

        rcvGioHang.setAdapter(adapter);

        loadGioHang();

        btnDatHang.setOnClickListener(v -> {
            if (list.isEmpty()) {
                Toast.makeText(CartActivity.this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
                finish();
            }
        });
    }

    private void initViews() {
        btnQuayLai = findViewById(R.id.btnCartQuayLai);
        rcvGioHang = findViewById(R.id.rcvGioHang);
        tvTongTien = findViewById(R.id.tvTongTienGioHang);
        btnDatHang = findViewById(R.id.btnTienHanhDatHang);
        layoutGioHangTrong = findViewById(R.id.layoutGioHangTrong);
    }

    private void loadGioHang() {
        RetrofitClient.getApiService().layGioHang(maNguoiDung).enqueue(new Callback<PhanHoiApi<List<MucGioHang>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<List<MucGioHang>>> call, Response<PhanHoiApi<List<MucGioHang>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    list.clear();
                    List<MucGioHang> duLieu = response.body().getDuLieu();
                    if (duLieu != null && !duLieu.isEmpty()) {
                        list.addAll(duLieu);
                        if (layoutGioHangTrong != null) layoutGioHangTrong.setVisibility(View.GONE);
                    } else {
                        if (layoutGioHangTrong != null) layoutGioHangTrong.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();

                    DecimalFormat df = new DecimalFormat("#,###");
                    tvTongTien.setText(df.format(response.body().getTongTienGioHang()) + " đ");
                } else {
                    if (layoutGioHangTrong != null) layoutGioHangTrong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<List<MucGioHang>>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi tải giỏ hàng!", Toast.LENGTH_SHORT).show();
                if (layoutGioHangTrong != null) layoutGioHangTrong.setVisibility(View.VISIBLE);
            }
        });
    }

    private void capNhatSoLuong(int maGioHang, int soLuongMoi) {
        if (soLuongMoi <= 0) {
            xoaGioHang(maGioHang);
            return;
        }

        RetrofitClient.getApiService().capNhatGioHang(maNguoiDung, maGioHang, soLuongMoi).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isThanhCong()) {
                        loadGioHang();
                    } else {
                        Toast.makeText(CartActivity.this, response.body().getThongBao(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi cập nhật số lượng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void xoaGioHang(int maGioHang) {
        RetrofitClient.getApiService().xoaGioHang(maNguoiDung, maGioHang).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    Toast.makeText(CartActivity.this, "Đã xóa khỏi giỏ hàng!", Toast.LENGTH_SHORT).show();
                    loadGioHang();
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi xóa sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
