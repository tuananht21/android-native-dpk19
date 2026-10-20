package com.legostore.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.legostore.MainActivity;
import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.DonHang;
import com.legostore.models.PhanHoiApi;
import com.legostore.models.SanPham;
import com.legostore.utils.UserSessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView btnMenu3Gach, btnQuickLogout;
    private CardView cardThemSP, cardDuyetDon;
    private TextView tvAdminDoanhThu, tvAdminDonCho, tvAdminTongSP, tvAdminTongKhach;
    private BarChart barChart;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        session = new UserSessionManager(this);

        initViews();
        setupSidebar();

        // Tải số liệu thực tế từ CSDL MySQL cho 4 thẻ thống kê
        loadThongKeTongQuan();

        // Tải dữ liệu biểu đồ doanh thu
        loadBieuDoThucTeTuDatabase();

        // Mở sidebar khi click nút 3 gạch
        btnMenu3Gach.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Click nhanh đăng xuất góc trên phải
        btnQuickLogout.setOnClickListener(v -> dangXuatAdmin());

        // Bấm thẻ quản lý nhanh trên dashboard
        cardThemSP.setOnClickListener(v -> startActivity(new Intent(this, AdminAddProductActivity.class)));
        cardDuyetDon.setOnClickListener(v -> startActivity(new Intent(this, AdminOrderManageActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại số liệu khi quay lại màn hình Admin
        loadThongKeTongQuan();
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.adminDrawerLayout);
        navigationView = findViewById(R.id.adminNavigationView);
        btnMenu3Gach = findViewById(R.id.btnAdminMenu3Gach);
        btnQuickLogout = findViewById(R.id.btnAdminNhanhDangXuat);

        cardThemSP = findViewById(R.id.cardAdminThemSP);
        cardDuyetDon = findViewById(R.id.cardAdminDuyetDon);

        // Ánh xạ 4 TextView hiển thị thông số thống kê
        tvAdminDoanhThu = findViewById(R.id.tvAdminDoanhThu);
        tvAdminDonCho = findViewById(R.id.tvAdminDonCho);
        tvAdminTongSP = findViewById(R.id.tvAdminTongSP);
        tvAdminTongKhach = findViewById(R.id.tvAdminTongKhach);

        barChart = findViewById(R.id.barChartDoanhThu);
    }

    private void setupSidebar() {
        View headerView = navigationView.getHeaderView(0);
        TextView tvNavName = headerView.findViewById(R.id.tvNavName);
        TextView tvNavEmail = headerView.findViewById(R.id.tvNavEmail);
        tvNavName.setText("Quản trị viên (Admin)");
        tvNavEmail.setText("ID: #" + session.getUserId());

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_admin_home) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_admin_add_product) {
                startActivity(new Intent(this, AdminAddProductActivity.class));
            } else if (id == R.id.nav_admin_orders) {
                startActivity(new Intent(this, AdminOrderManageActivity.class));
            } else if (id == R.id.nav_admin_to_user) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (id == R.id.nav_admin_logout) {
                dangXuatAdmin();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // Tải dữ liệu thực tế từ database cho 4 card thống kê
    private void loadThongKeTongQuan() {
        // 1. Tải danh sách tất cả đơn hàng để thống kê Doanh thu, Đơn chờ duyệt & Khách hàng
        RetrofitClient.getApiService().layDonHang(0).enqueue(new Callback<PhanHoiApi<List<DonHang>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<List<DonHang>>> call, Response<PhanHoiApi<List<DonHang>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    List<DonHang> listDon = response.body().getDuLieu();
                    if (listDon != null) {
                        double tongDoanhThu = 0;
                        int donCho = 0;

                        for (DonHang dh : listDon) {
                            if (!"DA_HUY".equalsIgnoreCase(dh.getTrangThai())) {
                                tongDoanhThu += dh.getTongTien();
                            }
                            if ("CHO_XAC_NHAN".equalsIgnoreCase(dh.getTrangThai())) {
                                donCho++;
                            }
                        }

                        DecimalFormat df = new DecimalFormat("#,###");
                        if (tvAdminDoanhThu != null) tvAdminDoanhThu.setText(df.format(tongDoanhThu) + " đ");
                        if (tvAdminDonCho != null) tvAdminDonCho.setText(donCho + " đơn");
                        if (tvAdminTongKhach != null) tvAdminTongKhach.setText(listDon.size() + " đơn");
                    }
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<List<DonHang>>> call, Throwable t) {
                Log.e("ADMIN_DEBUG", "Lỗi tải thống kê đơn hàng: " + t.getMessage());
            }
        });

        // 2. Tải danh sách sản phẩm để thống kê Tổng LEGO
        RetrofitClient.getApiService().laySanPham(1, 0, "").enqueue(new Callback<PhanHoiApi<List<SanPham>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<List<SanPham>>> call, Response<PhanHoiApi<List<SanPham>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    List<SanPham> listSP = response.body().getDuLieu();
                    if (listSP != null && tvAdminTongSP != null) {
                        tvAdminTongSP.setText(listSP.size() + " loại");
                    }
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<List<SanPham>>> call, Throwable t) {
                Log.e("ADMIN_DEBUG", "Lỗi tải thống kê sản phẩm: " + t.getMessage());
            }
        });
    }

    private void loadBieuDoThucTeTuDatabase() {
        RetrofitClient.getApiService().layThongKeDoanhThuAdmin().enqueue(new Callback<PhanHoiApi<Map<String, Double>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Map<String, Double>>> call, Response<PhanHoiApi<Map<String, Double>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    Map<String, Double> dataMap = response.body().getDuLieu();

                    if (dataMap != null) {
                        float t1 = dataMap.containsKey("tuan_1") ? dataMap.get("tuan_1").floatValue() / 1000000f : 0f;
                        float t2 = dataMap.containsKey("tuan_2") ? dataMap.get("tuan_2").floatValue() / 1000000f : 0f;
                        float t3 = dataMap.containsKey("tuan_3") ? dataMap.get("tuan_3").floatValue() / 1000000f : 0f;
                        float t4 = dataMap.containsKey("tuan_4") ? dataMap.get("tuan_4").floatValue() / 1000000f : 0f;

                        List<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0f, t1));
                        entries.add(new BarEntry(1f, t2));
                        entries.add(new BarEntry(2f, t3));
                        entries.add(new BarEntry(3f, t4));

                        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu (Tr.Đồng)");
                        dataSet.setColors(new int[]{
                                Color.parseColor("#FF9800"),
                                Color.parseColor("#FFCF00"),
                                Color.parseColor("#4CAF50"),
                                Color.parseColor("#D01012")
                        });
                        dataSet.setValueTextSize(11f);

                        BarData barData = new BarData(dataSet);
                        barChart.setData(barData);

                        String[] labels = new String[]{"Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4"};
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setDrawGridLines(false);

                        barChart.getDescription().setEnabled(false);
                        barChart.animateY(1000);
                        barChart.invalidate();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Map<String, Double>>> call, Throwable t) {
                Toast.makeText(AdminDashboardActivity.this, "Lỗi tải dữ liệu biểu đồ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dangXuatAdmin() {
        session.logout();
        Toast.makeText(this, "Admin đã đăng xuất!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
