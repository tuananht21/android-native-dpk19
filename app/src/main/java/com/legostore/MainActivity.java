package com.legostore;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.legostore.R;
import com.legostore.activities.CartActivity;
import com.legostore.activities.LoginActivity;
import com.legostore.activities.OrderListActivity;
import com.legostore.activities.ProductDetailActivity;
import com.legostore.activities.ProfileActivity;
import com.legostore.adapters.DanhMucAdapter;
import com.legostore.adapters.SanPhamAdapter;
import com.legostore.api.RetrofitClient;
import com.legostore.models.DanhMuc;
import com.legostore.models.PhanHoiApi;
import com.legostore.models.SanPham;
import com.legostore.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ImageView btnMenu3Gach, btnMoGioHang;
    private RecyclerView rcvDanhMuc, rcvSanPham;
    private EditText edtTimKiem;

    private Button btnTrangTruoc, btnTrangSau;
    private LinearLayout layoutNutTrangDong;

    private DanhMucAdapter danhMucAdapter;
    private SanPhamAdapter sanPhamAdapter;

    private List<DanhMuc> listDanhMuc = new ArrayList<>();
    private List<SanPham> listSanPham = new ArrayList<>();
    private UserSessionManager session;

    private int currentPage = 1;
    private int totalPages = 1;
    private int selectedMaDanhMuc = 0;
    private String currentSearchKeyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(this);

        initViews();
        setupNavigationDrawer();
        setupBottomNavigation();
        setupRecyclerViews();
        setupPaginationButtons();

        loadDanhMuc();
        chuyenTrang(1);

        btnMenu3Gach.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        if (btnMoGioHang != null) {
            btnMoGioHang.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        }

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchKeyword = s.toString().trim();
                chuyenTrang(1);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btnMenu3Gach = findViewById(R.id.btnMenu3Gach);
        btnMoGioHang = findViewById(R.id.btnMoGioHang);
        rcvDanhMuc = findViewById(R.id.rcvDanhMuc);
        rcvSanPham = findViewById(R.id.rcvSanPham);
        edtTimKiem = findViewById(R.id.edtTimKiem);

        btnTrangTruoc = findViewById(R.id.btnTrangTruoc);
        btnTrangSau = findViewById(R.id.btnTrangSau);
        layoutNutTrangDong = findViewById(R.id.layoutNutTrangDong);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(MainActivity.this, OrderListActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupNavigationDrawer() {
        View headerView = navigationView.getHeaderView(0);
        TextView tvNavName = headerView.findViewById(R.id.tvNavName);
        TextView tvNavEmail = headerView.findViewById(R.id.tvNavEmail);

        if (tvNavName != null) {
            tvNavName.setText("Xin chào, " + session.getFullName());
        }

        if (tvNavEmail != null) {
            tvNavEmail.setVisibility(View.GONE);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else if (id == R.id.nav_change_password) {
                hienThiDialogDoiMatKhau();
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(MainActivity.this, OrderListActivity.class));
            } else if (id == R.id.nav_logout) {
                session.logout();
                Toast.makeText(MainActivity.this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void hienThiDialogDoiMatKhau() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi mật khẩu");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_doi_mat_khau, null);
        EditText edtPassCu = view.findViewById(R.id.edtMatKhauCu);
        EditText edtPassMoi = view.findViewById(R.id.edtMatKhauMoi);

        builder.setView(view);
        builder.setPositiveButton("Đổi mật khẩu", (dialog, which) -> {
            String matKhauCu = edtPassCu.getText().toString().trim();
            String matKhauMoi = edtPassMoi.getText().toString().trim();

            if (!matKhauCu.isEmpty() && !matKhauMoi.isEmpty()) {
                RetrofitClient.getApiService().doiMatKhau(session.getUserId(), matKhauCu, matKhauMoi)
                        .enqueue(new Callback<PhanHoiApi<Void>>() {
                            @Override
                            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(MainActivity.this, response.body().getThongBao(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void setupRecyclerViews() {
        rcvDanhMuc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        danhMucAdapter = new DanhMucAdapter(this, listDanhMuc, danhMuc -> {
            selectedMaDanhMuc = danhMuc.getMaDanhMuc();
            chuyenTrang(1);
        });
        rcvDanhMuc.setAdapter(danhMucAdapter);

        rcvSanPham.setLayoutManager(new GridLayoutManager(this, 2));
        sanPhamAdapter = new SanPhamAdapter(this, listSanPham, sanPham -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("ma_san_pham", sanPham.getMaSanPham());
            intent.putExtra("ten_san_pham", sanPham.getTenSanPham());
            intent.putExtra("gia_ban", sanPham.getGiaBan());
            intent.putExtra("hinh_anh", sanPham.getHinhAnh());
            intent.putExtra("mo_ta", sanPham.getMoTa());
            startActivity(intent);
        });
        rcvSanPham.setAdapter(sanPhamAdapter);
    }

    private void setupPaginationButtons() {
        btnTrangTruoc.setOnClickListener(v -> {
            if (currentPage > 1) {
                chuyenTrang(currentPage - 1); // Đã sửa phép trừ
            }
        });

        btnTrangSau.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                chuyenTrang(currentPage + 1);
            }
        });
    }

    private void chuyenTrang(int page) {
        currentPage = page;
        loadSanPham(currentPage, selectedMaDanhMuc, currentSearchKeyword);
    }

    private void loadDanhMuc() {
        RetrofitClient.getApiService().layDanhMuc().enqueue(new Callback<PhanHoiApi<List<DanhMuc>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<List<DanhMuc>>> call, Response<PhanHoiApi<List<DanhMuc>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    listDanhMuc.clear();
                    listDanhMuc.addAll(response.body().getDuLieu());
                    danhMucAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<List<DanhMuc>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi tải danh mục!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSanPham(int page, int maDanhMuc, String tuKhoa) {
        RetrofitClient.getApiService().laySanPham(page, maDanhMuc, tuKhoa).enqueue(new Callback<PhanHoiApi<List<SanPham>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<List<SanPham>>> call, Response<PhanHoiApi<List<SanPham>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    List<SanPham> listMoi = response.body().getDuLieu();

                    int tongTrangServer = response.body().getTongSoTrang();
                    if (tongTrangServer > 0) {
                        totalPages = tongTrangServer;
                    } else if (listMoi != null && !listMoi.isEmpty()) {
                        if (listMoi.size() == 4) {
                            totalPages = Math.max(totalPages, page + 1);
                        } else {
                            totalPages = page;
                        }
                    } else {
                        totalPages = Math.max(1, page - 1); // Đã sửa phép trừ
                    }

                    listSanPham.clear();
                    if (listMoi != null && !listMoi.isEmpty()) {
                        listSanPham.addAll(listMoi);
                    } else {
                        Toast.makeText(MainActivity.this, "Trang này không có sản phẩm!", Toast.LENGTH_SHORT).show();
                    }
                    sanPhamAdapter.notifyDataSetChanged();

                    renderNutTrangDong();
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<List<SanPham>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi tải sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderNutTrangDong() {
        if (layoutNutTrangDong == null) return;
        layoutNutTrangDong.removeAllViews();

        float density = getResources().getDisplayMetrics().density;
        int size = (int) (40 * density);   // Đã sửa phép nhân
        int margin = (int) (6 * density); // Đã sửa phép nhân

        for (int i = 1; i <= totalPages; i++) {
            final int pageNum = i;
            AppCompatButton btn = new AppCompatButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(margin, 0, 0, 0);
            btn.setLayoutParams(params);
            btn.setText(String.valueOf(i));
            btn.setTextSize(14);
            btn.setTypeface(null, Typeface.BOLD);

            if (i == currentPage) {
                btn.setBackgroundColor(Color.parseColor("#D01012"));
                btn.setTextColor(Color.WHITE);
            } else {
                btn.setBackgroundColor(Color.parseColor("#E0E0E0"));
                btn.setTextColor(Color.parseColor("#333333"));
            }

            btn.setOnClickListener(v -> chuyenTrang(pageNum));
            layoutNutTrangDong.addView(btn);
        }
    }
}