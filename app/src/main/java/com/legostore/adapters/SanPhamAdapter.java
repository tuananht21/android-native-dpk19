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
import com.legostore.models.SanPham;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    private Context context;
    private List<SanPham> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SanPham sanPham);
    }

    public SanPhamAdapter(Context context, List<SanPham> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham = list.get(position);
        holder.tvTenSanPham.setText(sanPham.getTenSanPham());

        DecimalFormat df = new DecimalFormat("#,###");
        holder.tvGiaSanPham.setText(df.format(sanPham.getGiaBan()) + " đ");

        String imageUrl = RetrofitClient.BASE_URL + sanPham.getHinhAnh();
        Glide.with(context).load(imageUrl).into(holder.imgSanPham);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(sanPham));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSanPham;
        TextView tvTenSanPham, tvGiaSanPham;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGiaSanPham = itemView.findViewById(R.id.tvGiaSanPham);
        }
    }

    // Hàm dùng cho phân trang: Nối thêm dữ liệu mới vào danh sách hiện có
    public void addMoreData(List<SanPham> listMoi) {
        int startPosition = this.list.size();
        this.list.addAll(listMoi);
        notifyItemRangeInserted(startPosition, listMoi.size());
    }
}