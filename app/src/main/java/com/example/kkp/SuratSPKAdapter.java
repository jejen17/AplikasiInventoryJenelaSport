package com.example.kkp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import model.SpkResponse;

import java.util.List;

public class SuratSPKAdapter extends ArrayAdapter<SpkResponse.Spk> {

    public SuratSPKAdapter(@NonNull Context context, List<SpkResponse.Spk> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_surat_spk, parent, false);
        }

        SpkResponse.Spk spk = getItem(position);

        CardView cardRoot = convertView.findViewById(R.id.cardRoot);

        TextView tvNama = convertView.findViewById(R.id.tvNamaPelanggan);
        TextView tvTotal = convertView.findViewById(R.id.tvTotalItem);

        if (spk != null) {

            tvNama.setText(spk.getNamaPelanggan());
            tvTotal.setText("Total Item: " + spk.getTotalItems() + " Barang");

            String status = spk.getStatus();

            if (status != null && status.equalsIgnoreCase("Pending")) {
                cardRoot.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                tvNama.setTextColor(Color.parseColor("#FF000000"));
                tvTotal.setTextColor(Color.parseColor("#808080"));

            } else {
                cardRoot.setCardBackgroundColor(Color.parseColor("#75a8f0"));
                tvNama.setTextColor(Color.parseColor("#FFFFFFFF"));
                tvTotal.setTextColor(Color.parseColor("#FFFFFFFF"));
            }


            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DetailSurat.class);
                intent.putExtra("SPK_DATA", spk);
                getContext().startActivity(intent);
            });
        }

        return convertView;
    }
}