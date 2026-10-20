package com.legostore.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legostore.R;
import com.legostore.adapters.DonHangAdapter;
import com.legostore.api.RetrofitClient;
import com.legostore.models.DonHang;
import com.legostore.models.PhanHoiApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderManageActivity extends AppCompatActivity {

    private ImageView btnQuayLai;
    private RecyclerView rcvDonHang;
    private LinearLayout layoutLichSuTrong;
    private DonHangAdapter adapter;
    private List<DonHang> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initViews();

        if (btnQuayLai != null) {
            btnQuayLai.setOnClickListener(v -> finish());
        }

        rcvDonHang.setLayoutManager(new LinearLayoutManager(this));

        // Khởi chạy adapter ở chế độ Admin (isAdminMode = true)
        adapter = new DonHangAdapter(this, list, true, donHang -> hienThiDialogDuyetDon(donHang.getMaDonHang()));
        rcvDonHang.setAdapter(adapter);

        loadTatCaDonHang();
    }

    private void initViews() {
        btnQuayLai = findViewById(R.id.btnOrderQuayLai);
        rcvDonHang = findViewById(R.id.rcvDonHang);
        layoutLichSuTrong = findViewById(R.id.layoutLichSuTrong);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTatCaDonHang();
    }

    private void loadTatCaDonHang() {
        // Truyền 0 để lấy tất cả đơn hàng của mọi người dùng
        RetrofitClient.getApiService().layDonHang(0).enqueue(new Callback<PhanHoiApi<List<DonHang>>>() {
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
                Log.e("ADMIN_ORDER_DEBUG", "Lỗi tải đơn hàng: " + t.getMessage());
                Toast.makeText(AdminOrderManageActivity.this, "Lỗi kết nối máy chủ!", Toast.LENGTH_SHORT).show();
                if (layoutLichSuTrong != null) layoutLichSuTrong.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hienThiDialogDuyetDon(int maDonHang) {
        String[] statusOptions = {"CHO_XAC_NHAN", "DANG_XU_LY", "DANG_GIAO", "HOAN_THANH", "DA_HUY"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Duyệt / Đổi trạng thái đơn #" + maDonHang);
        builder.setItems(statusOptions, (dialog, which) -> {
            String selectedStatus = statusOptions[which];
            capNhatTrangThai(maDonHang, selectedStatus);
        });
        builder.show();
    }

    private void capNhatTrangThai(int maDonHang, String trangThai) {
        RetrofitClient.getApiService().capNhatDonHangAdmin(maDonHang, trangThai).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AdminOrderManageActivity.this, response.body().getThongBao(), Toast.LENGTH_SHORT).show();
                    loadTatCaDonHang();
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Toast.makeText(AdminOrderManageActivity.this, "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
