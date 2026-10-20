package com.legostore.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.legostore.R;
import com.legostore.models.DonHang;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    private Context context;
    private List<DonHang> list;
    private OnOrderCancelListener cancelListener;
    private boolean isAdminMode = false;

    public interface OnOrderCancelListener {
        void onHuyDon(DonHang donHang);
    }

    public DonHangAdapter(Context context, List<DonHang> list, OnOrderCancelListener cancelListener) {
        this.context = context;
        this.list = list;
        this.cancelListener = cancelListener;
    }

    public DonHangAdapter(Context context, List<DonHang> list, boolean isAdminMode, OnOrderCancelListener cancelListener) {
        this.context = context;
        this.list = list;
        this.isAdminMode = isAdminMode;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang order = list.get(position);
        holder.tvMa.setText("Mã đơn hàng: #" + order.getMaDonHang());

        DecimalFormat df = new DecimalFormat("#,###");
        holder.tvTong.setText(df.format(order.getTongTien()) + " đ");
        holder.tvTrangThai.setText("Trạng thái: " + order.getTrangThai());

        if (isAdminMode) {
            // Chế độ Admin: Hiển thị nút "Duyệt đơn" màu xanh lá và cho phép bấm cả card
            holder.btnHuy.setVisibility(View.VISIBLE);
            holder.btnHuy.setText("Duyệt đơn");
            holder.btnHuy.setBackgroundColor(Color.parseColor("#4CAF50"));
            holder.btnHuy.setOnClickListener(v -> cancelListener.onHuyDon(order));
            holder.itemView.setOnClickListener(v -> cancelListener.onHuyDon(order));
        } else {
            // Chế độ Khách hàng: Chỉ hiển thị nút Hủy đơn khi đơn ở trạng thái CHO_XAC_NHAN
            holder.btnHuy.setText("Hủy đơn");
            holder.btnHuy.setBackgroundColor(Color.parseColor("#D01012"));
            if ("CHO_XAC_NHAN".equalsIgnoreCase(order.getTrangThai())) {
                holder.btnHuy.setVisibility(View.VISIBLE);
                holder.btnHuy.setOnClickListener(v -> cancelListener.onHuyDon(order));
            } else {
                holder.btnHuy.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMa, tvTong, tvTrangThai;
        Button btnHuy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMa = itemView.findViewById(R.id.tvMaDonHang);
            tvTong = itemView.findViewById(R.id.tvTongTienDonHang);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThaiDonHang);
            btnHuy = itemView.findViewById(R.id.btnHuyDonHang);
        }
    }
}
