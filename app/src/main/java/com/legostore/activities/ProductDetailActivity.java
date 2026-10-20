package com.legostore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.PhanHoiApi;
import com.legostore.models.SanPham;
import com.legostore.utils.UserSessionManager;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView btnQuayLai, btnGioHang, imgChiTiet;
    private TextView tvTen, tvGia, tvMoTa, tvSoLuongMua;
    private Button btnGiam, btnTang, btnThemVaoGio;

    private int maSanPham;
    private int soLuongMua = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();

        Intent intent = getIntent();
        maSanPham = intent.getIntExtra("ma_san_pham", -1);
        String tenSP = intent.getStringExtra("ten_san_pham");
        double giaBan = intent.getDoubleExtra("gia_ban", 0);
        String hinhAnh = intent.getStringExtra("hinh_anh");
        String moTa = intent.getStringExtra("mo_ta");

        if (tenSP != null) {
            hienThiThongTin(tenSP, giaBan, hinhAnh, moTa);
        } else {
            loadThongTinTuApi();
        }

        setupListeners();
    }

    private void initViews() {
        btnQuayLai = findViewById(R.id.btnDetailQuayLai);
        btnGioHang = findViewById(R.id.btnDetailGioHang);
        imgChiTiet = findViewById(R.id.imgChiTietSanPham);

        tvTen = findViewById(R.id.tvTenChiTiet);
        tvGia = findViewById(R.id.tvGiaChiTiet);
        tvMoTa = findViewById(R.id.tvMoTaChiTiet);
        tvSoLuongMua = findViewById(R.id.tvSoLuongMua);

        btnGiam = findViewById(R.id.btnGiamSoLuong);
        btnTang = findViewById(R.id.btnTangSoLuong);
        btnThemVaoGio = findViewById(R.id.btnThemVaoGioHang);
    }

    private void hienThiThongTin(String ten, double gia, String hinhAnh, String moTa) {
        if (tvTen != null) tvTen.setText(ten);

        DecimalFormat df = new DecimalFormat("#,###");
        if (tvGia != null) tvGia.setText(df.format(gia) + " đ");

        if (tvMoTa != null && moTa != null && !moTa.isEmpty()) {
            tvMoTa.setText(moTa);
        }

        if (imgChiTiet != null && hinhAnh != null && !hinhAnh.isEmpty()) {
            String url = hinhAnh.startsWith("http") ? hinhAnh : RetrofitClient.BASE_URL + hinhAnh;
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_shopping_cart)
                    .error(R.drawable.ic_shopping_cart)
                    .into(imgChiTiet);
        }
    }

    private void setupListeners() {
        if (btnQuayLai != null) {
            btnQuayLai.setOnClickListener(v -> finish());
        }

        if (btnGioHang != null) {
            btnGioHang.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        }

        if (btnGiam != null) {
            btnGiam.setOnClickListener(v -> {
                if (soLuongMua > 1) {
                    soLuongMua--;
                    tvSoLuongMua.setText(String.valueOf(soLuongMua));
                }
            });
        }

        if (btnTang != null) {
            btnTang.setOnClickListener(v -> {
                soLuongMua++;
                tvSoLuongMua.setText(String.valueOf(soLuongMua));
            });
        }

        if (btnThemVaoGio != null) {
            btnThemVaoGio.setOnClickListener(v -> xuLyThemGioHang());
        }
    }

    private void loadThongTinTuApi() {
        RetrofitClient.getApiService().laySanPham(1, 0, "").enqueue(new Callback<PhanHoiApi<List<SanPham>>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<List<SanPham>>> call, Response<PhanHoiApi<List<SanPham>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    List<SanPham> list = response.body().getDuLieu();
                    if (list != null) {
                        for (SanPham sp : list) {
                            if (sp.getMaSanPham() == maSanPham) {
                                hienThiThongTin(sp.getTenSanPham(), sp.getGiaBan(), sp.getHinhAnh(), sp.getMoTa());
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<List<SanPham>>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi tải thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void xuLyThemGioHang() {
        UserSessionManager session = new UserSessionManager(this);
        int maNguoiDung = session.getUserId();

        if (maNguoiDung == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để mua hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().themGioHang(maNguoiDung, maSanPham, soLuongMua).enqueue(new Callback<PhanHoiApi<Void>>() {
            @Override
            public void onResponse(Call<PhanHoiApi<Void>> call, Response<PhanHoiApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isThanhCong()) {
                    Toast.makeText(ProductDetailActivity.this, "Đã thêm " + soLuongMua + " sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PhanHoiApi<Void>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi thêm giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
