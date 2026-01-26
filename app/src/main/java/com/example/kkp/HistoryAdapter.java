package com.example.kkp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import model.HistoryResponse;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<HistoryResponse.HistoryItem> list;

    public HistoryAdapter(Context context, List<HistoryResponse.HistoryItem> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List<HistoryResponse.HistoryItem> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryResponse.HistoryItem item = list.get(position);

        // Set Nama Barang & Size
        holder.tvNama.setText(item.getNamaBarang() + " (" + item.getUkuran() + ")");
        holder.tvTanggal.setText(item.getTanggal());

        if ("Masuk".equalsIgnoreCase(item.getType())) {
            //barang masuk
            holder.tvJumlah.setText("+" + item.getJumlah());
            holder.tvJumlah.setTextColor(Color.parseColor("#14B835"));
            holder.tvStatus.setText("Masuk");


            holder.imgIcon.setImageResource(R.drawable.arrow_bawah);
            holder.imgIcon.setColorFilter(Color.parseColor("#14B835"));

        } else {
            //barang keluar
            holder.tvJumlah.setText("-" + item.getJumlah());
            holder.tvJumlah.setTextColor(Color.parseColor("#FC756A"));
            holder.tvStatus.setText("Keluar");


            holder.imgIcon.setImageResource(R.drawable.arrow_atas);
            holder.imgIcon.setColorFilter(Color.parseColor("#FC756A"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal, tvJumlah, tvStatus;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaBarang);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}