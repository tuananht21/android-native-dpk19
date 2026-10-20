package com.legostore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.legostore.R;
import com.legostore.api.RetrofitClient;
import com.legostore.models.DanhMuc;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.ViewHolder> {

    private Context context;
    private List<DanhMuc> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DanhMuc danhMuc);
    }

    public DanhMucAdapter(Context context, List<DanhMuc> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_muc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMuc danhMuc = list.get(position);
        holder.tvTenDanhMuc.setText(danhMuc.getTenDanhMuc());

        String imageUrl = RetrofitClient.BASE_URL + danhMuc.getHinhAnh();
        Glide.with(context).load(imageUrl).into(holder.imgDanhMuc);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(danhMuc));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDanhMuc;
        TextView tvTenDanhMuc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDanhMuc = itemView.findViewById(R.id.imgDanhMuc);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
        }
    }
}