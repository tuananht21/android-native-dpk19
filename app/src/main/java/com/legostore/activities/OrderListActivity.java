package com.legostore.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legostore.R;
import com.legostore.adapters.DonHangAdapter;
import com.legostore.api.RetrofitClient;
import com.legostore.models.DonHang;
import com.legostore.models.PhanHoiApi;
import com.legostore.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListActivity extends AppCompatActivity {

    private ImageView btnQuayLai;
    private RecyclerView rcvDonHang;
    private LinearLayout layoutLichSuTrong;
    private DonHangAdapter adapter;
    private List<DonHang> list = new ArrayList<>();
    private int maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        UserSessionManager session = new UserSessionManager(this);
        maNguoiDung = session.getUserId();

        initViews();

        if (btnQuayLai != null) {
            btnQuayLai.setOnClickListener(v -> finish());
        }

        rcvDonHang.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DonHangAdapter(this, list, donHang -> xuLyHuyDonHang(donHang.getMaDonHang()));
        rcvDonHang.setAdapter(adapter);

        loadLichSuDonHang();
    }

    private void initViews() {
        btnQuayLai = findViewById(R.id.btnOrderQuayLai);
        rcvDonHang = findViewById(R.id.rcvDonHang);
        layoutLichSuTrong = findViewById(R.id.layoutLichSuTrong);
    }

    private void loadLichSuDonHang() {
        RetrofitClient.getApiService().layDonHang(maNguoiDung).enqueue(new Callback<PhanHoiApi<List<DonHang>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<List<DonHang>>> call, Response<PhanHoiApi<List<DonHang>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    list.clear();
                    List<DonHang> duLieu = response.body().getDuLieu();
                    if (duLieu != null && !duLieu.isEmpty()) {
                        list.addAll(duLieu);
                        if (layoutLichSuTrong != null) layoutLichSuTrong.setVisibility(View.GONE);
                    } else {
                        if (layoutLichSuTrong != null) layoutLichSuTrong.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    if (layoutLichSuTrong != null) layoutLichSuTrong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<List<DonHang>>> call, Throwable t) {
                Toast.makeText(OrderListActivity.this, "Lỗi tải lịch sử đơn hàng!", Toast.LENGTH_SHORT).show();
                if (layoutLichSuTrong != null) layoutLichSuTrong.setVisibility(View.VISIBLE);
            }
        });
    }

    private void xuLyHuyDonHang(int maDonHang) {
        RetrofitClient.getApiService().huyDonHang(maNguoiDung, maDonHang).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(OrderListActivity.this, response.body().getThongBao(), Toast.LENGTH_SHORT).show();
                    if (response.body().isThanhCong()) {
                        loadLichSuDonHang();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Toast.makeText(OrderListActivity.this, "Lỗi hủy đơn hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
