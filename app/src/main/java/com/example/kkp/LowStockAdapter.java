package com.example.kkp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import model.LowStockResponse;
import java.util.List;

public class LowStockAdapter extends RecyclerView.Adapter<LowStockAdapter.ViewHolder> {

    private Context context;
    private List<LowStockResponse.StockItem> list;

    public LowStockAdapter(Context context, List<LowStockResponse.StockItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stok_menipis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LowStockResponse.StockItem item = list.get(position);

        holder.tvNama.setText(item.getNamaProduk() + " (" + item.getSize() + ")");

        String info = "Sisa: " + item.getStokSaatIni() + " | Min: " + item.getMinStok();
        holder.tvDetail.setText(info);

        holder.tvKekurangan.setText("-" + item.getKekurangan());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvDetail, tvKekurangan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaProduk);
            tvDetail = itemView.findViewById(R.id.tvDetailStok);
            tvKekurangan = itemView.findViewById(R.id.tvKekurangan);
        }
    }
}