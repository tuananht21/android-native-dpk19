package com.legostore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.MucGioHang;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {

    private Context context;
    private List<MucGioHang> list;
    private OnCartItemChangeListener listener;

    public interface OnCartItemChangeListener {
        void onTangSoLuong(MucGioHang item);
        void onGiamSoLuong(MucGioHang item);
        void onXoaItem(MucGioHang item);
    }

    public GioHangAdapter(Context context, List<MucGioHang> list, OnCartItemChangeListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MucGioHang item = list.get(position);
        holder.tvTen.setText(item.getTenSanPham());

        DecimalFormat df = new DecimalFormat("#,###");
        holder.tvGia.setText(df.format(item.getGiaBan()) + " đ");
        holder.tvSoLuong.setText(String.valueOf(item.getSoLuong()));

        String imageUrl = RetrofitClient.BASE_URL + item.getHinhAnh();
        Glide.with(context).load(imageUrl).into(holder.img);

        holder.btnTang.setOnClickListener(v -> listener.onTangSoLuong(item));
        holder.btnGiam.setOnClickListener(v -> listener.onGiamSoLuong(item));
        holder.btnXoa.setOnClickListener(v -> listener.onXoaItem(item));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, btnXoa;
        TextView tvTen, tvGia, tvSoLuong;
        Button btnTang, btnGiam;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgGioHang);
            tvTen = itemView.findViewById(R.id.tvTenGioHang);
            tvGia = itemView.findViewById(R.id.tvGiaGioHang);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuongGioHang);
            btnTang = itemView.findViewById(R.id.btnTangSoLuong);
            btnGiam = itemView.findViewById(R.id.btnGiamSoLuong);
            btnXoa = itemView.findViewById(R.id.btnXoaMonGioHang);
        }
    }
}